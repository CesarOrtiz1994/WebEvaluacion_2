package com.idgs06.UTEQLive.friends.controller;

import com.idgs06.UTEQLive.friends.entity.Solicitudes;
import com.idgs06.UTEQLive.friends.service.IAmigosService;
import com.idgs06.UTEQLive.friends.service.ISolicitudesService;
import com.idgs06.UTEQLive.usuario.entity.Usuario;
import com.idgs06.UTEQLive.usuario.service.IUsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AmigosController {
    
    @Autowired
    private IAmigosService amigsServ;
    @Autowired
    private IUsuarioService userService;
    @Autowired
    private ISolicitudesService soliService;
    
    @GetMapping("/amigos/")
    public String pageAmigos(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = userService.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = soliService.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        model.addAttribute("menuActive", "amigos");
        model.addAttribute("amigos", amigsServ.findAllByUserA(authentication.getName()));
        return "friends/amigos";
    }
    
    @PostMapping("/amigos/eliminar")
    public String eliminarAmigo(Model model, @RequestParam("usera") String usera, @RequestParam("userb") String userb) {
        amigsServ.delete(usera, userb);
        return "redirect:";
    }
    
    
}
