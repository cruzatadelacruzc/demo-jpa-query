package com.example.demo.repository;

import com.example.demo.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Esto se llama composite o composicion donde puedes tener disponible
 * una implementacion personalizada de una consulta mas lo metodos que implementa Spring boot: save(), finById() ....
 */
public interface ProjectRepository extends JpaRepository<Project, Long> , ProjectRepositoryCustom {

    @Query("SELECT p FROM Project p JOIN User u ON p.id = u.project WHERE u.nombres = :name")
    Optional<Project> findOneByUserName(@Param("name") String name);

}
