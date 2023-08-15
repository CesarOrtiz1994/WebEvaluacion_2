package com.idgs06.UTEQLive.friends.controller;

import com.idgs06.UTEQLive.friends.entity.Amigos;
import com.idgs06.UTEQLive.friends.entity.Solicitudes;
import com.idgs06.UTEQLive.friends.service.IAmigosService;
import com.idgs06.UTEQLive.friends.service.ISolicitudesService;
import com.idgs06.UTEQLive.usuario.entity.Usuario;
import com.idgs06.UTEQLive.usuario.service.IUsuarioService;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/solicitudes")
public class SolicitudesController {
    
    @Autowired
    private ISolicitudesService solServ;
    @Autowired
    private IUsuarioService usuServ;
    @Autowired
    private IAmigosService amigsServ;
    
    @GetMapping("/")
    public String pageSolicitudes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = usuServ.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = solServ.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        model.addAttribute("menuActive", "solicitudes");
        model.addAttribute("solicitudes", listSoli);
        model.addAttribute("usuarios", new ArrayList<>() );
        return "friends/solicitudes";
    }
    
    @GetMapping("/buscar")
    public String buscarAmigos(Model model, @RequestParam("nombre") String nombre) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = usuServ.findByCorreo(authentication.getName());
        model.addAttribute("userSession", user);
        List<Solicitudes> listSoli = solServ.solicitudesRecibidas(authentication.getName());
        model.addAttribute("numSoli", listSoli.size());
        model.addAttribute("notificaciones", listSoli);
        model.addAttribute("menuActive", "solicitudes");
        model.addAttribute("solicitudes", listSoli);
        model.addAttribute("usuarios", usuServ.findAllByNombre(nombre) );
        return "friends/solicitudes";
    }
    
    @PostMapping("/rechazar")
    public String rechazarSolicitud(Model model, @RequestParam("id") int id) {
        solServ.deleteById(id);
        return "redirect:";
    }
    
    @PostMapping("/mandar-solicitud")
    public String mandarSolicitud(Model model, Solicitudes newSolicitud) {
        solServ.save(newSolicitud);
        return "redirect:";
    }
    
    @PostMapping("/aceptar")
    public String aceptarSolicitud(Model model, @RequestParam("nombre2") String nombre2, 
            @RequestParam("user1") String user1, @RequestParam("user2") String user2,
            @RequestParam("idSol") int idSol) {
        Amigos amigo = new Amigos();
        amigo.setUserA(user1);
        amigsServ.save(amigo, user2, idSol);
        return "redirect:";
    }
    
}
