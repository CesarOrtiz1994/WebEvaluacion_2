package com.idgs06.UTEQLive.usuario.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Entity
@Data
@Table(name = "authorities")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotEmpty
    private String authority;
    private String correo;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Rol) {
            Rol otro = (Rol) obj;
            return this.authority.equals(otro.authority);
        }
        return false;
    }

}
