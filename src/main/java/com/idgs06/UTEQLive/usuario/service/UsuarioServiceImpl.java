package com.idgs06.UTEQLive.usuario.service;

import com.idgs06.UTEQLive.friends.entity.Amigos;
import com.idgs06.UTEQLive.friends.repository.IAmigosRepository;
import com.idgs06.UTEQLive.security.WebSecurityConfig;
import com.idgs06.UTEQLive.usuario.entity.Rol;
import com.idgs06.UTEQLive.usuario.entity.Usuario;
import com.idgs06.UTEQLive.usuario.repository.IRolRepository;
import com.idgs06.UTEQLive.usuario.repository.IUsuarioRepository;
import com.idgs06.UTEQLive.utils.FilesUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioRepository repo;
    @Autowired
    private IRolRepository rolRepo;
    @Autowired
    private IAmigosRepository amigosRepo;

    @Override
    @Transactional(readOnly = true)
    public Usuario findByCorreo(String correo) {
        return repo.findByCorreoAndEnabled(correo, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return repo.findAllByEnabled(true);
    }

    @Override
    @Transactional
    public Usuario registro(Usuario usuario) {
        usuario.setPassword(WebSecurityConfig.passwordEncoder().encode(usuario.getPassword()));
        List<Rol> roles = new ArrayList<>();
        Rol rol = new Rol();
        rol.setAuthority("ROLE_ESTUDIANTE");
        rol.setCorreo(usuario.getCorreo());
        roles.add(rol);
        usuario.setAuthorities(roles);
        usuario.setEnabled(true);
        usuario.setFoto("avatar.png");
        return repo.save(usuario);
    }

    @Override
    @Transactional
    public void delete(String correo, String foto) {
        Usuario user = repo.findByCorreoAndEnabled(correo, true);
        user.setEnabled(false);
        repo.save(user);
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario, MultipartFile imageFoto, String rolUser) {
        List<Rol> roles = rolRepo.findAllByCorreo(usuario.getCorreo());
        if (roles.isEmpty()) {
            Rol rol = new Rol();
            rol.setAuthority("ROLE_ESTUDIANTE");
            rol.setCorreo(usuario.getCorreo());
            roles.add(rol);
            if (rolUser.equals("Maestro")) {
                Rol rolMaestr = new Rol();
                rolMaestr.setAuthority("ROLE_MAESTRO");
                rolMaestr.setCorreo(usuario.getCorreo());
                roles.add(rolMaestr);
            }
        } else {
            Rol admin = new Rol();
            admin.setAuthority("ROLE_ADMIN");
            Rol maestro = new Rol();
            maestro.setAuthority("ROLE_MAESTRO");
            if (!roles.contains(admin)) {
                if (roles.contains(maestro)) {
                    Rol maestroAnt = null;
                    for (Rol role : roles) {
                        if (role.equals(maestro) && "Estudiante".equals(rolUser)) {
                            maestroAnt = role;
                            break;
                        }
                    }
                    if(maestroAnt != null) {
                        roles.remove(maestroAnt);
                    }
                } else if ("Maestro".equals(rolUser)) {
                    Rol rolMaestr = new Rol();
                    rolMaestr.setAuthority("ROLE_MAESTRO");
                    rolMaestr.setCorreo(usuario.getCorreo());
                    roles.add(rolMaestr);
                }
            }
        }

        usuario.setAuthorities(roles);

        if (!imageFoto.getOriginalFilename().isEmpty()) {
            log.info("Guarda la foto de perfil");
            try {
                usuario.setFoto(FilesUtils.saveImgPerfil(imageFoto, usuario.getCorreo()));
            } catch (Exception ex) {
                Logger.getLogger(UsuarioServiceImpl.class.getName()).log(Level.SEVERE, "Error al guardar la foto de perfil.", ex);
            }
        } else if (usuario.getFoto().isEmpty()) {
            usuario.setFoto("avatar.png");
        }
        usuario.setPassword(WebSecurityConfig.passwordEncoder().encode(usuario.getPassword()));
        usuario.setEnabled(true);

        return repo.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllByNombre(String nombre) {
        List<Usuario> response = new ArrayList<>();
        List<Usuario> list = repo.findAllByNombre(nombre);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Amigos> amigos = amigosRepo.findAllByUserA(authentication.getName());

        for (Usuario usuario : list) {
            boolean isAdmin = false;
            boolean amigoEncontrado = false;
            for (Rol rol : usuario.getAuthorities()) {
                if (rol.getAuthority().equals("ROLE_ADMIN")) {
                    isAdmin = true;
                    break;
                }
            }
            for (Amigos amigo : amigos) {
                if (amigo.getUserB().getCorreo().equals(usuario.getCorreo())) {
                    amigoEncontrado = true;
                    break;
                }
            }
            if (!isAdmin && !amigoEncontrado && !usuario.getCorreo().equals(authentication.getName())) {
                response.add(usuario);
            }
        }
        return response;
    }

    @Override
    @Transactional
    public Usuario saveNewPass(Usuario usuario) {
        usuario.setPassword(WebSecurityConfig.passwordEncoder().encode(usuario.getPassword()));
        return repo.save(usuario);
    }

}
