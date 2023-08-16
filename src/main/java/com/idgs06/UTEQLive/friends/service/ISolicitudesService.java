package com.idgs06.UTEQLive.friends.service;

import com.idgs06.UTEQLive.friends.entity.Solicitudes;
import com.idgs06.UTEQLive.usuario.entity.Usuario;

import java.util.List;


public interface ISolicitudesService {

    List<Solicitudes> solicitudesRecibidas(String userRecibe);
    List<Solicitudes> solicitudesEnviadas(Usuario userEnvia);
    Solicitudes save(Solicitudes solicitud);
    void deleteById(int id);
}
