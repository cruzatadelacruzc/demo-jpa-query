package com.example.demo.service.dto;

import com.example.demo.domain.User;
import com.example.demo.service.UniqueValue;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
/**
 * A DTO for the {@link com.example.demo.domain.User} entity.
 */
@UniqueValue(entityClass = User.class, columnNames = {"nombres","apellidos"})
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "Solo para actualizar el usuario")
    private Long id;

    @NotBlank
    @ApiModelProperty(example = "Michel")
    private String nombres;

    @ApiModelProperty(example = "Frometa Buey")
    private String apellidos;

    @NotBlank
    @ApiModelProperty(example = "Male")
    private String sexo;

    @NotNull
    @ApiModelProperty(example = "29")
    private Integer edad;

    @ApiModelProperty(example = "true")
    private Boolean activo;

    @ApiModelProperty(example = "1", value = "Id del Proyecto creado")
    private Long projectId;

    public Long getId() {
        return id;
    }

    public UserDTO setId(Long id) {
        this.id = id;
        return this;
    }

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
