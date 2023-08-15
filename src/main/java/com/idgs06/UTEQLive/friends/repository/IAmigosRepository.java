package com.idgs06.UTEQLive.friends.repository;

import com.idgs06.UTEQLive.friends.entity.Amigos;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IAmigosRepository extends JpaRepository<Amigos, Integer>{

    List<Amigos> findAllByUserA(String userA);
    List<Amigos> findAllByUserAAndUserB_correo(String userA, String correo);
}
