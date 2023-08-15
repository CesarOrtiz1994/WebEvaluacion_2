package com.idgs06.UTEQLive.publication.service;

import com.idgs06.UTEQLive.publication.entity.Comentario;


public interface IComentarioService {

    Comentario save(Comentario comentario);
    void delete(int id);
    
}
