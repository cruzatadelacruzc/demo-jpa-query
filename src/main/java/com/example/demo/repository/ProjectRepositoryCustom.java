package com.example.demo.repository;

import com.example.demo.domain.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectRepositoryCustom {

    Optional<Project> findOneWithUsersByCentro(String centro);

    Optional<Project> findOneByCentro(String centro);

    List<Project> findAllByCentros(List<String> centros);
}
