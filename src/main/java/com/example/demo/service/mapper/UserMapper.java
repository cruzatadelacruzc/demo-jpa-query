package com.example.demo.service.mapper;

import com.example.demo.domain.Project;
import com.example.demo.domain.User;
import com.example.demo.service.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Un Factory para crea instancia en un solo lugar
 */
@Component
public class UserMapper {

    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNombres(user.getNombres());
        userDTO.setApellidos(user.getApellidos());
        userDTO.setEdad(user.getEdad());
        userDTO.setActivo(user.getActivo());
        userDTO.setSexo(user.getSexo());
        userDTO.setProjectId(user.getProject().getId());
        return userDTO;
    }

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }
        User user = new User();
        Project project = new Project();
        project.setId(userDTO.getProjectId());
        user.setProject(project);
        user.setId(userDTO.getId());
        user.setSexo(userDTO.getSexo());
        user.setApellidos(userDTO.getApellidos());
        user.setNombres(userDTO.getNombres());
        user.setEdad(userDTO.getEdad());
        user.setActivo(userDTO.getActivo());
        return user;
    }

    public List<UserDTO> toDTOs(List<User> users) {
        if (users == null) {
            return null;
        }
        List<UserDTO> dtos = new ArrayList<>(users.size());
        for (User user : users) {
            dtos.add(toDTO(user));
        }
        return dtos;
    }
}
