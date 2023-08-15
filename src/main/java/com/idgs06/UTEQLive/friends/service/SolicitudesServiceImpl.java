package com.idgs06.UTEQLive.friends.service;

import com.idgs06.UTEQLive.friends.entity.Solicitudes;
import com.idgs06.UTEQLive.friends.repository.ISolicitudesRepository;
import com.idgs06.UTEQLive.usuario.entity.Usuario;
import com.idgs06.UTEQLive.usuario.repository.IUsuarioRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SolicitudesServiceImpl implements ISolicitudesService{
    
    @Autowired
    private ISolicitudesRepository repo;
    @Autowired
    private IUsuarioRepository userRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Solicitudes> solicitudesRecibidas(String userRecibe) {
        return repo.findAllByUserRecibe(userRecibe);
    }

    @Override
    @Transactional
    public Solicitudes save(Solicitudes solicitud) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Usuario user = userRepo.findByCorreoAndEnabled(authentication.getName(), true);
        solicitud.setUserEnvia(user);
        return repo.save(solicitud);
    }

    @Override
    @Transactional
    public void deleteById(int id) {
        repo.deleteById(id);
    }

}
