package com.idgs06.UTEQLive.friends.service;

import com.idgs06.UTEQLive.friends.entity.Solicitudes;
import java.util.List;


public interface ISolicitudesService {

    List<Solicitudes> solicitudesRecibidas(String userRecibe);
    Solicitudes save(Solicitudes solicitud);
    void deleteById(int id);
}
