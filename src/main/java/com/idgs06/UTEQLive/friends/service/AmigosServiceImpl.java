package com.idgs06.UTEQLive.friends.service;

import com.idgs06.UTEQLive.friends.entity.Amigos;
import com.idgs06.UTEQLive.friends.repository.IAmigosRepository;
import com.idgs06.UTEQLive.usuario.entity.Usuario;
import com.idgs06.UTEQLive.usuario.repository.IUsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AmigosServiceImpl implements IAmigosService {

    @Autowired
    private IAmigosRepository repo;
    @Autowired
    private IUsuarioRepository userRepo;
    @Autowired
    private ISolicitudesService solRepo;

    @Override
    @Transactional(readOnly = true)
    public List<Amigos> findAllByUserA(String userA) {
        return repo.findAllByUserA(userA);
    }

    @Override
    @Transactional
    public Amigos save(Amigos amigo, String userb, int idSol) {
        solRepo.deleteById(idSol);
        Usuario userB = userRepo.findByCorreoAndEnabled(userb, true);
        amigo.setUserB(userB);
        repo.save(amigo);

        Usuario usera = userRepo.findByCorreoAndEnabled(amigo.getUserA(), true);
        Amigos amigoInv = new Amigos();
        amigoInv.setUserB(usera);
        amigoInv.setUserA(userb);
        return repo.save(amigoInv);
    }

    @Override
    @Transactional
    public void delete(String usera, String userb) {
        List<Amigos> list1 = repo.findAllByUserAAndUserB_correo(usera, userb);
        for (Amigos amigos : list1) {
            repo.deleteById(amigos.getId());
        }
        List<Amigos> list2 = repo.findAllByUserAAndUserB_correo(userb, usera);
        for (Amigos amigo : list2) {
            repo.deleteById(amigo.getId());
        }
    }

}
