package com.idgs06.UTEQLive.publication.service;

import com.idgs06.UTEQLive.publication.entity.Comentario;
import com.idgs06.UTEQLive.publication.repository.IComentarioRepository;
import com.idgs06.UTEQLive.publication.repository.IImagenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ComentarioServiceImpl implements IComentarioService{
    
    @Autowired
    private IComentarioRepository repo;
    

    @Override
    public Comentario save(Comentario comentario) {
        return repo.save(comentario);
    }

    @Override
    public void delete(int id) {
        repo.deleteById(id);
    }


}
