package com.example.demo.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * La entidades siempre deberian implementar {@link Serializable}
 */
@Entity
public class Project implements Serializable {

    /**
     * Siempre pon serialVersionUID = 1L cuando crees una entidad que extienda de {@link Serializable}
     * No debes crearle ni metodo Get ni Set
     */
    private static final long serialVersionUID = 1L;

    @Id
    @ApiModelProperty(hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ApiModelProperty(example = "AndroidLab")
    private String nombre;

    @ApiModelProperty(example = "CEGEL")
    private String centro;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "project", allowSetters = true)
    private Set<User> users = new HashSet<>();

    public Long getId() {
        return id;
    }

    public Project setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNombre() {
        return nombre;
    }

    public Project setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public String getCentro() {
        return centro;
    }

    public Project setCentro(String centro) {
        this.centro = centro;
        return this;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Project setUsers(Set<User> users) {
        this.users = users;
        return this;
    }

    public void addUser(User user) {
        this.users.add(user);
        user.setProject(this);
    }

    public void removeUser(User user) {
        this.users.remove(user);
        user.setProject(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Project)) return false;
        return id != null && id.equals(((Project) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", centro='" + centro + '\'' +
                '}';
    }
}
