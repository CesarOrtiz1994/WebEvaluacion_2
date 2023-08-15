package com.idgs06.UTEQLive.friends.service;

import com.idgs06.UTEQLive.friends.entity.Amigos;
import java.util.List;


public interface IAmigosService {

    List<Amigos> findAllByUserA(String userA);
    Amigos save(Amigos amigo, String userb, int idSol);
    void delete(String usera, String userb);
}
