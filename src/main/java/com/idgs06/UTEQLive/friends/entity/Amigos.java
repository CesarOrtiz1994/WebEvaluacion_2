package com.idgs06.UTEQLive.friends.entity;

import com.idgs06.UTEQLive.usuario.entity.Usuario;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Amigos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String userA;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userb")
    private Usuario userB;
    
}
