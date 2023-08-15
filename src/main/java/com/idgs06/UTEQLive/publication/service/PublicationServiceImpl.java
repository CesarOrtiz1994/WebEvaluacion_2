package com.idgs06.UTEQLive.publication.service;

import com.idgs06.UTEQLive.friends.entity.Amigos;
import com.idgs06.UTEQLive.friends.repository.IAmigosRepository;
import com.idgs06.UTEQLive.publication.entity.Imagen;
import com.idgs06.UTEQLive.publication.entity.Publication;
import com.idgs06.UTEQLive.publication.repository.IImagenRepository;
import com.idgs06.UTEQLive.publication.repository.IPublicationRepository;
import com.idgs06.UTEQLive.utils.FechasUtils;
import com.idgs06.UTEQLive.utils.FilesUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class PublicationServiceImpl implements IPublicationService {

    @Autowired
    private IPublicationRepository publiRepo;
    @Autowired
    private IAmigosRepository amigosRepo;
    @Autowired
    private IImagenRepository imgRepo;

    @Override
    @Transactional
    public Publication save(Publication publication, List<MultipartFile> imagenes, String nameUser) {
        publication.setFecha(FechasUtils.getFechaNow());
        try {
            List<Imagen> listImagenes = new FilesUtils().saveImages(imagenes);
            for (Imagen imagen : listImagenes) {
                if (!imagen.getRuta().isEmpty()) {
                    imagen.setPublication(publication);
                }
            }
            publication.setImagenes(listImagenes);
        } catch (Exception ex) {
            Logger.getLogger(PublicationServiceImpl.class.getName()).log(Level.SEVERE, "Error al guardar archivos", ex);
        }
        publication.setActivo(true);
        return publiRepo.save(publication);
    }

    @Override
    @Transactional(readOnly = true)
    public Publication findById(int id) {
        return publiRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Publication> findAll() {
        List<Publication> response = new ArrayList<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        List<Publication> publicaciones = publiRepo.findAllByActivoOrderByIdDesc(true);
        List<Amigos> amigos = amigosRepo.findAllByUserA(username);
        for (Publication publi : publicaciones) {
            if ("amigos".equals(publi.getVisibilidad())) {
                boolean amigoEncontrado = false;
                for (Amigos amigo : amigos) {
                    if (amigo.getUserB().getCorreo().equals(publi.getUser().getCorreo())) {
                        amigoEncontrado = true;
                        break;
                    }
                }
                if (amigoEncontrado || username.equals(publi.getUser().getCorreo())) {
                    response.add(publi);
                }
            } else {
                response.add(publi);
            }
        }
        return response;
    }

    @Override
    @Transactional
    public void delete(int id) {
        Publication publication = publiRepo.findById(id).orElse(null);
        for (Imagen imagen : publication.getImagenes()) {
            FilesUtils.deleteFile(imagen.getRuta());
        }
        publiRepo.deleteById(id);
    }

    @Override
    public void deleteImg(int id, String ruta) {
        FilesUtils.deleteFile(ruta);
        imgRepo.deleteById(id);
    }

}
