package com.example.demo.web.rest;

import com.example.demo.DemoApplication;
import com.example.demo.domain.Project;
import com.example.demo.domain.User;
import com.example.demo.repository.ProjectRepository;
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

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;

import static com.example.demo.web.rest.TestUtil.createObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = DemoApplication.class)
public class ProjectResourceIT {

    private static final String DEFAULT_CENTRO = "CEDAI";
    private static final String UPDATED_CENTRO = "CIEGE";

    private static final String DEFAULT_NOMBRES = "SIGEF";
    private static final String UPDATED_NOMBRES = "ANDROIDLAB";

    @Autowired
    private ProjectRepository repository;

    private Project project;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void initTest() {
        project = new Project()
                .setCentro(DEFAULT_CENTRO)
                .setNombre(DEFAULT_NOMBRES);
    }

    @Test
    @Transactional
    void create() throws Exception {
        int databaseSizeInitial = repository.findAll().size();

        this.mvc.perform(post("/api/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createObjectMapper().writeValueAsBytes(project)))
                .andExpect(status().isCreated());
        // Validate the Poor in the database
        List<Project> projectList = repository.findAll();
        assertThat(projectList).hasSize(databaseSizeInitial + 1);
        Project testProject = projectList.get(projectList.size() -1);
        assertThat(testProject.getCentro()).isEqualTo(DEFAULT_CENTRO);
        assertThat(testProject.getNombre()).isEqualTo(DEFAULT_NOMBRES);
    }

    @Test
    void getProjectByCenter() throws Exception {
        User user = new User()
            .setActivo(true)
            .setApellidos("Cruzata")
            .setEdad(29)
            .setNombres("Cesar")
            .setSexo("Masculino")
            .setProject(project);
        project.addUser(user);
        repository.saveAndFlush(project);

        mvc.perform(get("/api/findproject/{centro}", project.getCentro()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.centro").value(DEFAULT_CENTRO))
                .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRES));
    }

    @Test
    @Transactional
    void deleteById() throws Exception {
        User user = new User()
                .setActivo(true)
                .setApellidos("Cruzata")
                .setEdad(29)
                .setNombres("Cesar")
                .setSexo("Masculino")
                .setProject(project);
        project.addUser(user);
        repository.saveAndFlush(project);

        int databaseSizeBeforeUpdate = repository.findAll().size();

        mvc.perform(delete("/api/projects/{id}", project.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        List<Project> projectList = repository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate -1);
    }
}
