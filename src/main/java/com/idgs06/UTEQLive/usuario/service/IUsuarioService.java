package com.idgs06.UTEQLive.usuario.service;

import com.idgs06.UTEQLive.usuario.entity.Usuario;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface IUsuarioService {
    
    Usuario findByCorreo(String correo);
    List<Usuario> findAll();
    Usuario registro(Usuario usuario);
    void delete(String correo, String foto);
    Usuario saveNewPass(Usuario usuario);
    Usuario save(Usuario usuario, MultipartFile imageFoto, String rolUser);
    List<Usuario> findAllByNombre(String nombre);

}
