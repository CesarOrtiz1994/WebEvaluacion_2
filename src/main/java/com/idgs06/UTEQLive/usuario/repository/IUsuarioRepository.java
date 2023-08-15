package com.idgs06.UTEQLive.usuario.repository;

import com.idgs06.UTEQLive.usuario.entity.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IUsuarioRepository extends JpaRepository<Usuario, String> {
    
    List<Usuario> findAllByEnabled(boolean enabled);
    Usuario findByCorreoAndEnabled(String correo, boolean enabled);
    @Query("SELECT u FROM Usuario u WHERE ( u.nombres LIKE :nombre% OR u.apellidos LIKE :nombre% ) and u.enabled = true ")
    List<Usuario> findAllByNombre(@Param("nombre") String nombre);

}
