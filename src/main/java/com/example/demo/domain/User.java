package com.example.demo.domain;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * La entidades siempre deberian implementar {@link Serializable}
 */
@Entity
public class User implements Serializable {
    /**
     * Siempre pon serialVersionUID = 1L cuando crees una entidad que extienda de {@link Serializable}
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @ManyToOne
    private Project project;

    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNombres() {
        return nombres;
    }

    public User setNombres(String nombres) {
        this.nombres = nombres;
        return this;
    }

    public String getApellidos() {
        return apellidos;
    }

    public User setApellidos(String apellidos) {
        this.apellidos = apellidos;
        return this;
    }

    public String getSexo() {
        return sexo;
    }

    public User setSexo(String sexo) {
        this.sexo = sexo;
        return this;
    }

    public Integer getEdad() {
        return edad;
    }

    public User setEdad(Integer edad) {
        this.edad = edad;
        return this;
    }

    public Boolean getActivo() {
        return activo;
    }

    public User setActivo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public Project getProject() {
        return project;
    }

    public User setProject(Project project) {
        this.project = project;
        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        return id != null && id.equals(((User) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", sexo='" + sexo + '\'' +
                ", edad=" + edad +
                ", activo=" + activo +
                '}';
    }
}
