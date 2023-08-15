package com.idgs06.UTEQLive.usuario.controller;

import com.idgs06.UTEQLive.friends.entity.Solicitudes;
import com.idgs06.UTEQLive.friends.service.ISolicitudesService;
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
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
public class UsuarioController {

    @Autowired
    private IUsuarioService service;
    @Autowired
    private ISolicitudesService soliService;

    @GetMapping("/usuarios/")
    public String page(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = service.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        model.addAttribute("menuActive", "usuarios");
        model.addAttribute("usuarios", service.findAll());
        return "usuarios/usuarios";
    }

    @GetMapping("/usuarios/agregar")
    public String formAgregar(Model model, Usuario usuario) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = service.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        model.addAttribute("menuActive", "usuarios");
        model.addAttribute("operacion", "agregar");
        model.addAttribute("tituloOper", "Agregar nuevo usuario");
        model.addAttribute("textBtn", "Agregar");
        return "usuarios/formUsuarios";
    }

    @GetMapping("/usuarios/editar/{correo}")
    public String formEditar(Model model, Usuario usuario) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = service.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        model.addAttribute("menuActive", "usuarios");
        model.addAttribute("operacion", "editar");
        model.addAttribute("tituloOper", "Editar usuario");
        model.addAttribute("textBtn", "Guardar");
        model.addAttribute("usuario", service.findByCorreo(usuario.getCorreo()));
        return "usuarios/formUsuarios";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(Model model, @Valid Usuario usuario, BindingResult results,
            @RequestParam("operacion") String operacion, @RequestParam("imageFoto") MultipartFile imageFoto,
            @RequestParam("rolUser") String rolUser) {
        List<MultipartFile> files = new ArrayList<>();
        files.add(imageFoto);
        if (!FilesUtils.validaExtensionesImagenes(files)) {
            results.rejectValue("foto", "error.foto", "Solo se permiten extensiones .JPG, .JPEG y .PNG");
        }
        if (!FechasUtils.esMenorDe18(usuario.getFechaNac())) {
            results.rejectValue("fechaNac", "error.fechaNac", "Fecha de nacimiento invalida.");
        }

        if (results.hasErrors()) {
            if ("agregar".equals(operacion)) {
                model.addAttribute("tituloOper", "Agregar nuevo usuario");
                model.addAttribute("textBtn", "Agregar");
            } else {
                model.addAttribute("tituloOper", "Editar usuario");
                model.addAttribute("textBtn", "Guardar");
            }

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Usuario user = service.findByCorreo(authentication.getName());
            model.addAttribute("userSession", user);
            List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
            model.addAttribute("numSoli", listSoli.size());
            model.addAttribute("notificaciones", listSoli);
            model.addAttribute("menuActive", "usuarios");
            model.addAttribute("operacion", operacion);
            model.addAttribute("rolUser", rolUser);

            return "usuarios/formUsuarios";
        }
        service.save(usuario, imageFoto, rolUser);
        return "redirect:";
    }

    @PostMapping("/usuarios/eliminar")
    public String eliminarUsuario(Model model, @RequestParam("correo") String correo, @RequestParam("foto") String foto) {
        service.delete(correo, foto);
        return "redirect:";
    }

}
