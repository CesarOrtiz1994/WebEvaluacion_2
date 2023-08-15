package com.idgs06.UTEQLive.utils;

import com.idgs06.UTEQLive.publication.entity.Imagen;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
public class FilesUtils {

    private final Path rootFolderPublics = Paths.get("src//main//resources//static//img//publics");
    private static final Path rootFolderPerfil = Paths.get("src//main//resources//static//img//perfil");

    public static String saveImgPerfil(MultipartFile file, String nombre) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = nombre.replaceFirst("@uteq.edu.mx", "").trim() + extension;
        Path pathFinal = rootFolderPerfil.resolve(newFilename);
        if(Files.exists(pathFinal)) {
            Files.delete(pathFinal);
        }
        Files.copy(file.getInputStream(), pathFinal);
        Thread.sleep(2000); // espera 2 segundos
        return "perfil/" + newFilename;
    }

    public Imagen saveFile(MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String newFilename = UUID.randomUUID().toString() + extension;
        Path pathFinal = rootFolderPublics.resolve(newFilename);
        Files.copy(file.getInputStream(), pathFinal);
        Imagen imagen = new Imagen();
        imagen.setRuta("publics/" + newFilename);
        return imagen;
    }

    public List<Imagen> saveImages(List<MultipartFile> files) throws Exception {
        List<Imagen> resp = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                resp.add(saveFile(file));
            }
        }
        Thread.sleep(2000); // espera 2 segundos
        return resp;
    }

    public static boolean validaExtensionesImagenes(List<MultipartFile> files) {
        if (files.get(0).getOriginalFilename().isBlank()) {
            return true;
        }
        for (MultipartFile file : files) {
            String nombreArchivo = file.getOriginalFilename();
            if (!nombreArchivo.endsWith(".jpg")
                    && !nombreArchivo.endsWith(".jpeg")
                    && !nombreArchivo.endsWith(".png")) {
                return false;
            }
        }
        return true;
    }
    
    public static void deleteFile(String ruta) {
        
        Path path = Paths.get("src/main/resources/static/img/"+ruta);
        try {
            Files.delete(path);
        } catch (IOException ex) {
            Logger.getLogger(FilesUtils.class.getName()).log(Level.SEVERE, "Error al intentar eliminar el archivo.", ex);
        }

    }

}
