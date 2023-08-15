package com.idgs06.UTEQLive.publication.controller;

import com.idgs06.UTEQLive.friends.entity.Solicitudes;
import com.idgs06.UTEQLive.friends.service.ISolicitudesService;
import com.idgs06.UTEQLive.publication.entity.Comentario;
import com.idgs06.UTEQLive.publication.entity.Imagen;
import com.idgs06.UTEQLive.publication.entity.Publication;
import com.idgs06.UTEQLive.publication.service.IComentarioService;
import com.idgs06.UTEQLive.publication.service.IPublicationService;
import com.idgs06.UTEQLive.usuario.entity.Rol;
import com.idgs06.UTEQLive.usuario.entity.Usuario;
import com.idgs06.UTEQLive.usuario.service.IUsuarioService;
import com.idgs06.UTEQLive.utils.FechasUtils;
import com.idgs06.UTEQLive.utils.FilesUtils;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class PublicationsController {

    @Autowired
    private IPublicationService service;
    @Autowired
    private IUsuarioService userService;
    @Autowired
    private IComentarioService comentService;
    @Autowired
    private ISolicitudesService soliService;

    @GetMapping("/")
    public String page(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = userService.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        Comentario comnt = new Comentario();
        comnt.setUser(user);
        model.addAttribute("menuActive", "inicio");
        model.addAttribute("publicaciones", service.findAll());
        model.addAttribute("comentario", comnt);

        return "publications/home";
    }

    @GetMapping("/publicacion/agregar")
    public String formAgregar(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = userService.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        Publication publication = new Publication();
        publication.setUser(user);
        model.addAttribute("menuActive", "inicio");
        model.addAttribute("operacion", "agregar");
        model.addAttribute("tituloOper", "Agregar nueva publicación");
        model.addAttribute("textBtn", "Publicar");
        model.addAttribute("publication", publication);
        return "publications/formPublicacion";
    }

    @GetMapping("/publicacion/editar/{id}")
    public String formEditar(Model model, Publication publication) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = userService.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        Publication publi = service.findById(publication.getId());
        List<Imagen> imagenes = publi.getImagenes();
        publi.setImagenes(null);
        model.addAttribute("menuActive", "inicio");
        model.addAttribute("operacion", "editar");
        model.addAttribute("tituloOper", "Editar publicación");
        model.addAttribute("textBtn", "Guardar cambios");
        model.addAttribute("publication", publi);
        model.addAttribute("listImg", imagenes);
        return "publications/formPublicacion";
    }

    @PostMapping("/publicacion/guardar")
    public String guardarPublicacion(Model model, @Valid Publication publication, BindingResult results,
            @RequestParam("operacion") String operacion, @RequestParam("multiImagenes") List<MultipartFile> imagenes) {
        if (!FilesUtils.validaExtensionesImagenes(imagenes)) {
            results.rejectValue("imagenes", "error.imagenes", "Solo se permiten extensiones .JPG, .JPEG y .PNG");
        }
        if (results.hasErrors()) {
            if ("agregar".equals(operacion)) {
                model.addAttribute("tituloOper", "Agregar nueva publicación");
                model.addAttribute("textBtn", "Publicar");
            } else {
                model.addAttribute("tituloOper", "Editar publicación");
                model.addAttribute("textBtn", "Guardar cambios");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario user = userService.findByCorreo(authentication.getName());
            model.addAttribute("userSession", user);
            List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
            model.addAttribute("numSoli", listSoli.size());
            model.addAttribute("notificaciones", listSoli);
            model.addAttribute("multiImagenes", imagenes);
            model.addAttribute("operacion", operacion);
            model.addAttribute("menuActive", "inicio");
            return "publications/formPublicacion";
        }
        service.save(publication, imagenes, publication.getUser().getCorreo());
        return "redirect:/";
    }

    @PostMapping("/publicacion/eliminar")
    public String eliminarPubli(Model model, @RequestParam("id") int id) {
        service.delete(id);
        return "redirect:/";
    }

    @PostMapping("/comentario/guardar")
    public String guardarComentario(Model model, @Valid Comentario comentario, BindingResult results,
            @RequestParam("idPubli") String idPubli) {
        if (results.hasErrors()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario user = userService.findByCorreo(authentication.getName());
            model.addAttribute("userSession", user);
            List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
            model.addAttribute("numSoli", listSoli.size());
            model.addAttribute("notificaciones", listSoli);
            model.addAttribute("menuActive", "inicio");
            model.addAttribute("publicaciones", service.findAll());
            return "publications/home";
        }
        comentario.setPublication(service.findById(Integer.parseInt(idPubli)));
        comentService.save(comentario);
        return "redirect:/";
    }

    @PostMapping("/comentario/eliminar")
    public String eliminarComentario(Model model, @RequestParam("id") int id) {
        comentService.delete(id);
        return "redirect:/";
    }

    @PostMapping("/imagen/eliminar")
    public String eliminarImg(Model model, @RequestParam("id") int id, @RequestParam("idPublicacion") int idPublicacion,
            @RequestParam("rutaFile") String rutaFile) {
        service.deleteImg(id, rutaFile);
        return "redirect:/publicacion/editar/" + idPublicacion;
    }

    @GetMapping(value = "/perfil/{correo}", produces = "application/json")
    public @ResponseBody
    Usuario perfilUser(Usuario usuario) {
        return userService.findByCorreo(usuario.getCorreo());
    }

//    @PreAuthorize("authentication.name == #correo")
    @GetMapping("/perfil/editar")
    public String formEditar(Model model, Usuario usuario) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = userService.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        model.addAttribute("operacion", "perfil");
        model.addAttribute("tituloOper", "Editar perfil");
        model.addAttribute("textBtn", "Guardar");
        model.addAttribute("usuario", user);

        return "usuarios/formPerfil";
    }

    @PostMapping("/perfil/guardar")
    public String guardarUsuario(Model model, @Valid Usuario usuario, BindingResult results,
            @RequestParam("operacion") String operacion, @RequestParam("imageFoto") MultipartFile imageFoto) {
        List<MultipartFile> files = new ArrayList<>();
        files.add(imageFoto);
        if (!FilesUtils.validaExtensionesImagenes(files)) {
            results.rejectValue("foto", "error.foto", "Solo se permiten extensiones .JPG, .JPEG y .PNG");
        }
        if (!FechasUtils.esMenorDe18(usuario.getFechaNac())) {
            results.rejectValue("fechaNac", "error.fechaNac", "Fecha de nacimiento invalida.");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = userService.findByCorreo(authentication.getName());
        if (results.hasErrors()) {
            model.addAttribute("tituloOper", "Editar perfil");
            model.addAttribute("textBtn", "Guardar");
            model.addAttribute("userSession", user);
            List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
            model.addAttribute("numSoli", listSoli.size());
            model.addAttribute("notificaciones", listSoli);
            model.addAttribute("menuActive", "usuarios");
            model.addAttribute("operacion", operacion);

            return "usuarios/formPerfil";
        }
        String rolUser = "";
        for (Rol rol : user.getAuthorities()) {
            switch (rol.getAuthority()) {
                case "ROLE_ADMIN":
                    rolUser = "Admin";
                    break;
                case "ROLE_MAESTRO":
                    rolUser = "Maestro";
                    break;
                default:
                    rolUser = "Estudiante";
                    break;
            }
        }
        userService.save(usuario, imageFoto, rolUser);
        return "redirect:/";
    }

}
