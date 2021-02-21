package com.example.demo.web.rest;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.example.demo.service.dto.UserDTO;
import com.example.demo.service.mapper.UserMapper;
import com.example.demo.web.rest.error.BadRequestException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Api(tags = {"Recurso Usuario"})
@RestController
@RequestMapping("/api")
public class UserResource {
    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final UserService userService;

    private final UserMapper userMapper;

    public UserResource(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    /**
     * Crea un usuario para que puedas probar las consultas
     * La anotacion {@code @Valid} valida el {@link UserDTO} Donde se encuentran las restriciones que validadn los datos
     * de entrada que vienen del Frontend o como quieras decir, Valida el formulario
     *
     * @param userDTO contiene la informacion del usuario
     * @return el estatus {@code 201 created} y en el cuerpo una instancia {@link User}.
     * @throws URISyntaxException si la sintaxi de la uri esta incorrecta.
     */
    @PostMapping("/users")
    @ApiOperation(value = "Crear un usuario")
    public ResponseEntity<UserDTO> creatUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        logger.debug("Peticion REST para crear un usuario: {}", userDTO);
        if (userDTO.getId() != null) {
            throw new BadRequestException("Id debe ser nulo");
        }
        UserDTO userCreado = userService.salvarUsuario(userDTO);
        return ResponseEntity.created(new URI("/findUser/" + userCreado.getNombres())).body(userCreado);
    }

    /**
     * Endpoint que devuelve una lista de usuario
     *
     * @return Lista de usuarion con {@code 200 Ok}
     */
    @GetMapping("/users/all")
    @ApiOperation(value = "Listar usuarios")
    public ResponseEntity<List<UserDTO>> findAll() {
        logger.debug("Peticion REST para mostrar todos los Usuarios");
        return ResponseEntity.ok(userService.findAll());
    }

    /**
     * {@code PUT /users }: Modificar la instancia {@link User}.
     *
     * @return Usuario
     */
    @PutMapping("/users")
    @ApiOperation(value = "Actualizar estado del usuario dado un nombre")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO) {
        logger.debug("Petecion REST para actualizar el usuario :{}", userDTO);
        if (userDTO.getId() == null) {
            throw new BadRequestException("El id del usuario es  nulo");
        }
        UserDTO usuarioActualizado = userService.salvarUsuario(userDTO);
        return ResponseEntity.ok(usuarioActualizado);
    }

    /**
     * {@code GET /users } : Obtener un usuario dado el ID
     *
     * @param id identificador del usuario
     * @return la {@link ResponseEntity} con estatus {@code 200 (Ok)} y en cuepo la instacia de {@link User} o,
     * cuerpo vacio con estatus {@code 404 (Not Found)}
     */
    @GetMapping("/users/{id}")
    @ApiOperation(value = "Obtener un usuario dado el identificador")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        logger.debug("Petecion REST para obetener un usuario con ID : {}", id);
        return userService.getUserWithProjectById(id).map(user -> ResponseEntity.ok(userMapper.toDTO(user)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * {@code DELETE /users/{id}} : Eliminar un usuario dado el ID.
     *
     * @param id de usuario
     * @return la {@link ResponseEntity} con estatado {@code 204 {Not Content}}
     */
    @DeleteMapping("/useres/{id}")
    @ApiOperation(value = "Obtener un usuario dado el identificador")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        logger.debug("Peticion REST par eliminar un uuario dado ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
