package com.idgs06.UTEQLive.friends.repository;

import com.idgs06.UTEQLive.friends.entity.Solicitudes;
import java.util.List;

import com.idgs06.UTEQLive.usuario.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISolicitudesRepository extends JpaRepository<Solicitudes, Integer>{
    List<Solicitudes> findAllByUserRecibe(String userRecibe);

    List<Solicitudes> findAllByUserEnvia(Usuario userEnvia);
}
