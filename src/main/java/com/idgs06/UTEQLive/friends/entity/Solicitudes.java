package com.idgs06.UTEQLive.friends.entity;

import com.idgs06.UTEQLive.usuario.entity.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
public class Solicitudes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String userRecibe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_envia")
    private Usuario userEnvia;
    
}
