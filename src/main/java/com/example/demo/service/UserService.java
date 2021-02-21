package com.example.demo.service;

import com.example.demo.domain.Project;
import com.example.demo.domain.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.dto.UserDTO;
import com.example.demo.service.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository repository;

    private final UserMapper userMapper;

    private final ProjectRepository projectRepository;

    public UserService(UserRepository repository, UserMapper userMapper, ProjectRepository projectRepository) {
        this.repository = repository;
        this.userMapper = userMapper;
        this.projectRepository = projectRepository;
    }

    /**
     * Crear un usuario
     *
     * @param userDTO informacion para crear un usuario
     * @return la instancia {@link User}.
     * @throws NullPointerException si el Id del {@link Project} es nulo
     */
    public UserDTO salvarUsuario(UserDTO userDTO) {
        if (!projectRepository.findById(userDTO.getProjectId()).isPresent()) {
            throw new NullPointerException("Proyecto no encontrado para el ID: " + userDTO.getProjectId());
        }
        User user = userMapper.toEntity(userDTO);
        repository.save(user);
        LOGGER.debug("Informacion del usuarion salvada: {}", user);
        return userMapper.toDTO(user);
    }

    /**
     * Mostrar todos los usuario
     *
     * @return lista de {@link UserDTO}
     */
    public List<UserDTO> findAll() {
        LOGGER.debug("Peticion para mostrar todos los usuarios");
        return userMapper.toDTOs(repository.findAll());
    }

    /**
     * Obtener un usuario dado un ID
     *
     * @return una instancia de {@link UserDTO}
     */
    public Optional<User> getUserWithProjectById(Long id) {
        LOGGER.debug("Peticion para obtener un usuario dado ID: {}", id);
        return repository.findOneWithProjectById(id);
    }

    /**
     * Eliminar un usuario dado un ID
     *
     * @param id del usuario a eliminar
     */
    public void deleteUser(Long id) {
        repository.findById(id).ifPresent(repository::delete);
        LOGGER.debug("Usuario eliminado con ID: {}", id);
    }
}
