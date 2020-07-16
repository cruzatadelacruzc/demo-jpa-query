package com.example.demo.repository;

import com.example.demo.domain.User;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u from User u")
    List<User> findAllUser();

    @Query("SELECT u from User u")
    List<User> findAllSortedUserByName(Sort sort);

    @Query("SELECT u from User u where u.activo = 1")
    List<User> findAllActivatedUser();

    // No me gusta esta forma de pasar parametros
    @Query("SELECT u from User u where u.nombres = ?1 AND u.activo = ?2")
    Optional<User> findOneUserGivenParametersNotUsingParam(String name, boolean status);

    // Prefiero esta forma de pasar parametro
    @Query(value = "SELECT u from User u where u.nombres = :name AND u.activo = :status")
    Optional<User> findOneUserGivenParametersUsingParam(@Param("name") String nombre, @Param("status") boolean estado);

    @Query(value = "SELECT u from User u where u.nombres IN :names" )
    List<User> findUserGivenNameList(@Param("names") List<String> names);

    @Transactional
    @Modifying
    @Query("UPDATE User u set u.activo = :status where u.nombres = :name")
    int updateUserSetStatusForName(@Param("status") boolean status, @Param("name") String name);

}
