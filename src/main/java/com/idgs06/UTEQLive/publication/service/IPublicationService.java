package com.idgs06.UTEQLive.publication.service;

import com.idgs06.UTEQLive.publication.entity.Publication;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;


public interface IPublicationService {

    public Publication save(Publication publication, List<MultipartFile> imagenes, String nameUser);
    public Publication findById(int id);
    public List<Publication> findAll();
    public void delete(int id);
    void deleteImg(int id, String ruta);
}
