package com.idgs06.UTEQLive.controller;

import com.idgs06.UTEQLive.usuario.entity.Usuario;
import com.idgs06.UTEQLive.usuario.service.IUsuarioService;
import com.idgs06.UTEQLive.utils.FechasUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class InicioController {
    
    @Autowired
    private IUsuarioService service;
    
    @GetMapping("/registro")
    public String pageRegistro(Model model, Usuario usuario) {
        return "registro";
    }
    
    @PostMapping("/registro/registrar")
    public String registrarUser(Model model, @Valid Usuario usuario, BindingResult results) {
        if(!FechasUtils.esMenorDe18(usuario.getFechaNac())) {
            results.rejectValue("fechaNac", "error.fechaNac", "Fecha de nacimiento invalida, solo a partir de 18 años antes de la fecha actual.");
        }
        
        if (results.hasErrors()) {
            return "registro";
        }
        service.registro(usuario);
        return "redirect:login";
    }
    
    @GetMapping("/recuperar-pass")
    public String pageRecuperaPass(Model model, Usuario usuario) {
        model.addAttribute("errorEmail", "");
        model.addAttribute("nuevoPass", "close");
        return "recuperaPass";
    }
    
    @PostMapping("/recuperar-pass/validar-email")
    public String validarEmail(Model model, @Valid Usuario usuario, BindingResult results) {
        Usuario userIn = usuario;
        if (results.hasErrors() && results.getErrorCount() > 6) {
            model.addAttribute("errorEmail", "");
            model.addAttribute("nuevoPass", "close");
            return "recuperaPass";
        }
        Usuario userExist = service.findByCorreo(userIn.getCorreo());
        if (userExist != null) {
            model.addAttribute("nuevoPass", "casi");
            model.addAttribute("errorEmail", "");
            model.addAttribute("usuario", userExist);
            return "recuperaPass";
        }
        model.addAttribute("usuario", userIn);
        model.addAttribute("nuevoPass", "close");
        model.addAttribute("errorEmail", "No existe un usuario con ese correo.");
        return "recuperaPass";
    }
    
    @PostMapping("/recuperar-pass/validar-secret")
    public String validarSecret(Model model, @Valid Usuario usuario, BindingResult results) {
        Usuario userIn = usuario;
        if (results.hasErrors() && results.getErrorCount() > 4) {
            model.addAttribute("errorEmail", "");
            model.addAttribute("nuevoPass", "casi");
            return "recuperaPass";
        }
        Usuario userExist = service.findByCorreo(userIn.getCorreo());
        if (userIn.getSecretQuestion().equalsIgnoreCase(userExist.getSecretQuestion())) {
            model.addAttribute("nuevoPass", "open");
            model.addAttribute("errorEmail", "");
            model.addAttribute("usuario", userExist);
            return "recuperaPass";
        }
        model.addAttribute("usuario", userIn);
        model.addAttribute("nuevoPass", "casi");
        model.addAttribute("errorEmail", "Respuesta secreta incorrecta.");
        return "recuperaPass";
    }

    @PostMapping("/recuperar-pass/cambiar-pass")
    public String cambiarPass(Model model, @Valid Usuario usuario, BindingResult results) {
        if (results.hasErrors() && results.getErrorCount() > 5) {
            model.addAttribute("nuevoPass", "open");
            model.addAttribute("errorEmail", "");
            return "recuperaPass";
        }
        Usuario userNewPass = service.findByCorreo(usuario.getCorreo());
        userNewPass.setPassword(usuario.getPassword());
        service.saveNewPass(userNewPass);
        return "redirect:login";
    }
    
}
