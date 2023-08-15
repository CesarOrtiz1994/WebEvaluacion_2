package com.idgs06.UTEQLive.usuario.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class Usuario {
    @Id
    @Column(length = 50, unique = true)
    @NotEmpty
    @Pattern(regexp = "^[\\w-\\.]+@uteq\\.edu\\.mx$", message = "El correo debe ser de dominio @uteq.edu.mx y solo se permiten letras (mayúsculas o minúsculas), números, \"-\", \"_\" y \".\"")
    private String correo;
    @NotEmpty
    @Size(min = 8, max = 60)
    private String password;
    @NotEmpty
    private String nombres;
    @NotEmpty
    private String apellidos;
    @NotEmpty
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha debe tener el formato yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private String fechaNac;
    private String foto;
    private String carrera;
    private String prepa;
    private String trabaja;
    private String empresa;
    private String lugarNac;
    private String sitSentimental;
    private String gustos;
    @NotEmpty
    private String question;
    @NotEmpty
    private String secretQuestion;
    private boolean enabled;
    // trae todos los roles del usuario
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "correo")
    private List<Rol> authorities;
}
