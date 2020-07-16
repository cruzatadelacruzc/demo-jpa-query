package com.example.demo.web.rest;

import com.example.demo.domain.Project;
import com.example.demo.repository.ProjectRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Api(tags = {"Recurso Proyecto"})
@RestController
public class ProjectResource {
    private static final Logger logger = LoggerFactory.getLogger(ProjectResource.class);

    private final ProjectRepository projectRepository;

    public ProjectResource(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    /**
     * {@code POST /projects }Crea un proyecto para que puedas probar las consultas
     *
     * @param project
     * @return Usuario con {@code 201 created}
     * @throws URISyntaxException
     */
    @PostMapping("/projects")
    @ApiOperation(value = "Crear un proyecto")
    public ResponseEntity<Project> creatproject(@Valid @RequestBody Project project) throws URISyntaxException {
        logger.info("Creando el proyecto {}", project);
        projectRepository.saveAndFlush(project);
        return ResponseEntity.created(new URI("/findproject/" + project.getNombre())).body(project);
    }

    /**
     * {@code Get /findproject/centro } que devuelve un proyecto dado el centro
     *
     * @return Un proyecto con {@code 200 Ok}
     */
    @GetMapping("/findproject/{centro}")
    @ApiOperation(value = "Obtener proyecto dado centro")
    public ResponseEntity<Project> findProject(@PathVariable String centro) {
        logger.info("Mostrando Proyecto dado el centro: {}", centro);
        Optional<Project> result = projectRepository.findOneByCentro(centro);
        return result.map(ResponseEntity::ok).orElse(new ResponseEntity(
                "Proyecto en el centro " + centro + " no encontrado"
                , HttpStatus.NOT_FOUND));
    }

    /**
     * {@code Get /findprojectgivenusername} Devuelve un proyecto dado el nombre del usuario que prtenesca
     * al proyecto
     *
     * @return Un proyecto con {@code 200 Ok}
     */
    @GetMapping("/findprojectgivenusername/{name}")
    @ApiOperation(value = "Obtener proyecto dado nombre de un usuario")
    public ResponseEntity<Project> findProjectGivenUserName(@PathVariable String name) {
        logger.info("Mostrando Proyecto dado el nombre de un usuario: {}", name);
        Optional<Project> result = projectRepository.findOneByUserName(name);
        return result.map(ResponseEntity::ok).orElse(new ResponseEntity(
                "El proyecto donde pertenece el usuarion " + name + " no fue encontrado"
                , HttpStatus.NOT_FOUND));
    }

    /**
     * {@code Post /findprojectgivencentros} Devuelve proyectos proyecto dado una lista de centros
     * al proyecto
     *
     * Aca en este metodo estoy esperando recivir un listado de String ej: ["CEGEL", "CEIGE"] -> Estructura del JSON
     * Diferente al {@code findUserGivenNameList()} en {@link UserResource} qu espero una clase. Esto es para variar
     * ambas vias estan bien
     * @return Lista de proyecto con {@code 200 Ok}
     */
    @PostMapping("/findprojectgivencentros")
    @ApiOperation(value = "Obtener proyecto dado listado de centros")
    public ResponseEntity<List<Project>> findProjectByCentros(@RequestBody List<String> centros) {
        logger.info("Buscando proyectos dado este listado de centro: {}", centros);
        return ResponseEntity.ok(projectRepository.findAllByCentros(centros));
    }
}
