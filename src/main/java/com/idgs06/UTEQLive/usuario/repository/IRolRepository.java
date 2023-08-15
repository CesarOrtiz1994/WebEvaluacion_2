package com.idgs06.UTEQLive.usuario.repository;

import com.idgs06.UTEQLive.usuario.entity.Rol;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IRolRepository extends JpaRepository<Rol, Integer> {

    List<Rol> findAllByCorreo(String correo);
}
