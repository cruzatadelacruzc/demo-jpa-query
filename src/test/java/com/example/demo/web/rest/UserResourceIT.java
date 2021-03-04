package com.example.demo.web.rest;

import com.example.demo.DemoApplication;
import com.example.demo.domain.Project;
import com.example.demo.domain.User;
import com.example.demo.repository.ProjectRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.dto.UserDTO;
import com.example.demo.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.web.rest.TestUtil.createObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = DemoApplication.class)
public class UserResourceIT {

    private static final String DEFAULT_NOMBRES = "MICHEL";
    private static final String UPDATED_NOMBRES = "LAZARO";

    private static final String DEFAULT_APELLIDOS = "FROMETA";
    private static final String UPDATED_APELLIDOS = "BUEY";

    private static final String DEFAULT_SEXO = "MASCULINO";
    private static final String UPDATED_SEXO = "BIXESUAL";

    private static final Integer DEFAULT_EDAD = 29;
    private static final Integer UPDATED_EDAD = 28;

    private static final Boolean DEFAULT_ACTIVO = true;
    private static final Boolean UPDATED_ACTIVO = false;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository repository;

    @Autowired
    private MockMvc mvc;

    private Project project;

    private User user;


    @BeforeEach
    public void initTest() {
        user = new User();
        user.setEdad(DEFAULT_EDAD)
                .setSexo(DEFAULT_SEXO)
                .setActivo(DEFAULT_ACTIVO)
                .setNombres(DEFAULT_NOMBRES)
                .setApellidos(DEFAULT_APELLIDOS);
        this.project = new Project();
        this.project.setCentro("CIDI");
        this.project.setNombre("Chivatientes");
        projectRepository.saveAndFlush(this.project);
        user.setProject(this.project);
    }

    @Test
    @Transactional
    void createUser() throws Exception {
        int databaseSizeInitial = repository.findAll().size();

        UserDTO userDTO = userMapper.toDTO(this.user);
        this.mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectMapper().writeValueAsBytes(userDTO)))
                .andExpect(status().isCreated());

        // Validate the Poor in the database
        List<User> all = repository.findAll();
        assertThat(all).hasSize(databaseSizeInitial + 1);
        User testUser = all.get(all.size() - 1);
        assertThat(testUser.getEdad()).isEqualTo(DEFAULT_EDAD);
        assertThat(testUser.getActivo()).isEqualTo(DEFAULT_ACTIVO);
        assertThat(testUser.getNombres()).isEqualTo(DEFAULT_NOMBRES);
        assertThat(testUser.getApellidos()).isEqualTo(DEFAULT_APELLIDOS);
        assertThat(testUser.getSexo()).isEqualTo(DEFAULT_SEXO);
        assertThat(testUser.getProject()).isEqualTo(this.project);
    }

    @Test
    @Transactional
    void createUserUniqueValueConstraintViolated() throws Exception {
        //Initialize database
        User user2 = new User()
                .setEdad(DEFAULT_EDAD)
                .setSexo(UPDATED_SEXO)
                .setActivo(UPDATED_ACTIVO)
                .setNombres(DEFAULT_NOMBRES)
                .setApellidos(DEFAULT_APELLIDOS);
        repository.saveAndFlush(user2);
        int databaseSizeInitial = repository.findAll().size();

        UserDTO userDTO = userMapper.toDTO(this.user);
        this.mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectMapper().writeValueAsBytes(userDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Poor in the database
        List<User> all = repository.findAll();
        assertThat(all).hasSize(databaseSizeInitial);
    }

    @Test
    @Transactional
    void createUserWithIdNotNull() throws Exception {
        int databaseSizeInitial = repository.findAll().size();

        UserDTO userDTO = userMapper.toDTO(this.user);
        userDTO.setId(1L);
        this.mvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectMapper().writeValueAsBytes(userDTO)))
                .andExpect(status().isBadRequest());

        // Validate the Poor in the database
        List<User> all = repository.findAll();
        assertThat(all).hasSize(databaseSizeInitial);
    }

    @Test
    void getUserById() throws Exception {
        // Initialize the database
        repository.saveAndFlush(user);

        mvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sexo").value(DEFAULT_SEXO))
                .andExpect(jsonPath("$.nombres").value(DEFAULT_NOMBRES))
                .andExpect(jsonPath("$.apellidos").value(DEFAULT_APELLIDOS))
                .andExpect(jsonPath("$.activo").value(DEFAULT_ACTIVO))
                .andExpect(jsonPath("$.edad").value(DEFAULT_EDAD));
    }

    @Test
    void getUserWithNoExistingId() throws Exception {

        mvc.perform(get("/api/users/-1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void getAllUsers() throws Exception {
        // Initialize the database
        repository.saveAndFlush(user);

        mvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*].sexo").value(DEFAULT_SEXO))
                .andExpect(jsonPath("$.[*].nombres").value(DEFAULT_NOMBRES))
                .andExpect(jsonPath("$.[*].apellidos").value(DEFAULT_APELLIDOS))
                .andExpect(jsonPath("$.[*].activo").value(DEFAULT_ACTIVO))
                .andExpect(jsonPath("$.[*].edad").value(DEFAULT_EDAD));

    }

    @Test
    @Transactional
    void updateUser() throws Exception {
       // Initialize the database
        repository.saveAndFlush(user);
        int databaseSizeInitial = repository.findAll().size();

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEdad(UPDATED_EDAD);
        userDTO.setSexo(UPDATED_SEXO);
        userDTO.setNombres(UPDATED_NOMBRES);
        userDTO.setApellidos(UPDATED_APELLIDOS);
        userDTO.setActivo(UPDATED_ACTIVO);
        userDTO.setProjectId(this.project.getId());

        mvc.perform(put("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectMapper().writeValueAsBytes(userDTO)))
                .andExpect(status().isOk());

               // Validate the User in the database
        List<User> userList = repository.findAll();
        assertThat(userList).hasSize(databaseSizeInitial);
        User testUser = userList.get(databaseSizeInitial - 1);
        assertThat(testUser.getSexo()).isEqualTo(UPDATED_SEXO);
        assertThat(testUser.getNombres()).isEqualTo(UPDATED_NOMBRES);
        assertThat(testUser.getApellidos()).isEqualTo(UPDATED_APELLIDOS);
        assertThat(testUser.getActivo()).isEqualTo(UPDATED_ACTIVO);
    }

}
