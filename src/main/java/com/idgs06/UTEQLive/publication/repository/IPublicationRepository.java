package com.idgs06.UTEQLive.publication.repository;

import com.idgs06.UTEQLive.publication.entity.Publication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPublicationRepository extends JpaRepository<Publication, Integer>{
    
    public List<Publication> findAllByActivoOrderByIdDesc(boolean activo);
}
