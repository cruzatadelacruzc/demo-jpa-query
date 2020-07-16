package com.example.demo.web.rest;

import com.example.demo.domain.Project;
import com.example.demo.domain.User;
import com.example.demo.repository.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@Api(tags = {"Recurso Usuario"})
@RestController
public class UserResource {
    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);
    private final UserRepository userRepository;

    public UserResource(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Crea un usuario para que puedas probar las consultas
     *
     * @param userDTO
     * @return Usuario con {@code 201 created}
     * @throws URISyntaxException
     */
    @PostMapping("/users")
    @ApiOperation(value = "Crear un usuario")
    public ResponseEntity<User> creatUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        logger.info("Creando el usuario {}", userDTO);
        User user = new User();
        user.setNombres(userDTO.getNombres());
        user.setApellidos(userDTO.getApellidos());
        user.setEdad(userDTO.getEdad());
        user.setActivo(userDTO.getActivo());
        user.setSexo(userDTO.getSexo());
        Project project = new Project();// Aqui se deberia primero mandar a buscar Proyecto haber si existe
        project.setId(userDTO.getProjectId());
        user.setProject(project);
        userRepository.save(user);
        return ResponseEntity.created(new URI("/findUser/" + user.getNombres())).body(user);
    }

    /**
     * Endpoint que devuelve una lista de usuario
     *
     * @return Lista de usuarion con {@code 200 Ok}
     */
    @GetMapping("/findall")
    @ApiOperation(value = "Listar usuarios")
    public ResponseEntity<List<User>> findAll() {
        logger.info("Mostrando todos los Usuarios");
        return ResponseEntity.ok(userRepository.findAllUser());
    }

    /**
     * {@code Get /findallsorteduser }: Listar usuarios ordenados por el nombre
     *
     * @return Lista de usuarios ordenados ascedentemente
     */
    @GetMapping("/findallsorteduser")
    @ApiOperation(value = "Listar usuarios ordenados por nombre")
    public ResponseEntity<List<User>> findAllSortedUsers() {
        logger.info("Mostrando usuarios ordenados por nombre");
        return ResponseEntity.ok(userRepository.findAllSortedUserByName(
                new Sort(Sort.Direction.ASC, "nombres"))
        );
    }

    /**
     * {@code Get /findallsorteduserbylengthname }: Listar usuarios ordenados por el tamaño del nombre
     *
     * @return Lista de usuarios ordenados
     */
    @GetMapping("/findallsorteduserbylengthname")
    @ApiOperation(value = "Listar usuarios ordenados por el tamaño del nombre descendientemente")
    public ResponseEntity<List<User>> findAllSortedUsersLengthName() {
        logger.info("Mostrando usuarios ordenados por el tamaño del nombre");
        return ResponseEntity.ok(userRepository.findAllSortedUserByName(
                JpaSort.unsafe("LENGTH(nombres)"))
        );
    }

    /**
     * {@code Get /findallactivateduser }: Listar usuarios donde columna activado se true == 1
     *
     * @return Lista de usuarios activados
     */
    @GetMapping("/findallactivateduser")
    @ApiOperation(value = "Listar usuarios activados")
    public ResponseEntity<List<User>> findAllActivatedUsers() {
        logger.info("Mostrando usuarios activados");
        return ResponseEntity.ok(userRepository.findAllActivatedUser());
    }

    /**
     * {@code Get /findoneusergivennameactivov1 }: Listar usuario dado nombre y estado
     *
     * @return Lista usuario
     */
    @GetMapping("/findoneusergivennameactivov1/{name}/{status}")
    @ApiOperation(value = "Listar usuario por nombre y estado")
    public ResponseEntity<User> findOneUserGivenParametersNotUsingParam(@PathVariable String name, @PathVariable boolean status) {
        logger.info("Mostrando usuario v1 dado name {}, y estado {}", name, status);
        Optional<User> result = userRepository.findOneUserGivenParametersNotUsingParam(name, status);
        return result.map(ResponseEntity::ok).orElse(new ResponseEntity(
                "Usuario no encontrado con el nombre: " + name + " y estado: " + status
                , HttpStatus.NOT_FOUND));
    }

    /**
     * {@code Get /findoneusergivennameactivov1 }: Listar usuario dado nombre y estado
     *
     * @return Lista usuario
     */
    @GetMapping("/findoneusergivennameactivov2/{name}/{status}")
    @ApiOperation(value = "Listar usuario por nombre y estado")
    public ResponseEntity<User> findOneUserGivenParametersUsingParam(@PathVariable String name, @PathVariable boolean status) {
        logger.info("Mostrando usuario v2 dado nombre {}, y estado {}", name, status);
        Optional<User> result = userRepository.findOneUserGivenParametersUsingParam(name, status);
        return result.map(ResponseEntity::ok).orElse(new ResponseEntity(
                "Usuario no encontrado con el nombre: " + name + " y estado: " + status
                , HttpStatus.NOT_FOUND));
    }

    /**
     * {@code Get /findaoneusergivennameactivov1 }: Listar usuario dado nombre y estado
     *
     * @return Lista usuario
     */
    @PostMapping("/findusergivennames")
    @ApiOperation(value = "Listar usuarios por nombres")
    public ResponseEntity<List<User>> findUserGivenNameList(@RequestBody NamesVM namesVM) {
        logger.info("Mostrando usuarios dado lista de nombres {}", namesVM.getNameList());
        return ResponseEntity.ok(userRepository.findUserGivenNameList(namesVM.getNameList()));
    }

    /**
     * {@code Put /users }: Modificar estado del usuario dado nombre y estado
     *
     * @return Usuario
     */
    @PutMapping("/users")
    @ApiOperation(value = "Actualizar estado del usuario dado un nombre")
    public ResponseEntity<String> updateUserSetStatusForName(@RequestBody UpdateVM updateVM) {
        logger.info("Actualizando estado a {} del usuario dado nombre: {}", updateVM.getStatus(), updateVM.getName());
        int result = userRepository.updateUserSetStatusForName(updateVM.getStatus(), updateVM.getName());
        return ResponseEntity.ok("Cantidad de tuplas afectadas: " + result);
    }

    // Clase interna para maperar el modelo que proviene desde exterior View Model
    @ApiModel(value = "View Model para la lista de nombres")
    private static class NamesVM {
        private List<String> nameList;

        public List<String> getNameList() {
            return nameList;
        }

        public NamesVM setNameList(List<String> nameList) {
            this.nameList = nameList;
            return this;
        }
    }

    @ApiModel(value = "View Model para actualizar")
    private static class UpdateVM {
        @ApiModelProperty(example = "Eliodanis")
        private String name;
        @ApiModelProperty(example = "false")
        private Boolean status;

        public String getName() {
            return name;
        }

        public UpdateVM setName(String name) {
            this.name = name;
            return this;
        }

        public Boolean getStatus() {
            return status;
        }

        public UpdateVM setStatus(Boolean status) {
            this.status = status;
            return this;
        }
    }

    // Esta clase deberia crearse  en el paquete de service pero ahora lo hago acá
    private static class UserDTO {

        @NotBlank
        @ApiModelProperty(example = "Eliodanis")
        private String nombres;

        @ApiModelProperty(example = "Maceo")
        private String apellidos;

        @NotBlank
        @ApiModelProperty(example = "Male")
        private String sexo;

        @NotNull
        @ApiModelProperty(example = "28")
        private Integer edad;

        @ApiModelProperty(example = "true")
        private Boolean activo;

        @ApiModelProperty(example = "1", value = "Id del Proyecto creado")
        private Long projectId;

        public String getNombres() {
            return nombres;
        }

        public UserDTO setNombres(String nombres) {
            this.nombres = nombres;
            return this;
        }

        public String getApellidos() {
            return apellidos;
        }

        public UserDTO setApellidos(String apellidos) {
            this.apellidos = apellidos;
            return this;
        }

        public String getSexo() {
            return sexo;
        }

        public UserDTO setSexo(String sexo) {
            this.sexo = sexo;
            return this;
        }

        public Integer getEdad() {
            return edad;
        }

        public UserDTO setEdad(Integer edad) {
            this.edad = edad;
            return this;
        }

        public Boolean getActivo() {
            return activo;
        }

        public UserDTO setActivo(Boolean activo) {
            this.activo = activo;
            return this;
        }

        public Long getProjectId() {
            return projectId;
        }

        public UserDTO setProjectId(Long projectId) {
            this.projectId = projectId;
            return this;
        }
    }
}
