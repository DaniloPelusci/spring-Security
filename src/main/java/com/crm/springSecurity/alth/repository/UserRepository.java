package com.crm.springSecurity.alth.repository;

import com.crm.springSecurity.alth.modelSecurity.User;
import com.crm.springSecurity.model.dto.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByAuthoritiesDescription(String description);

    @Query("select new com.crm.springSecurity.model.dto.UserDTO(u.id, u.username, u.nome, u.email, u.telefone) from User u where u.id = :id")
    UserDTO getUserById(@Param("id") Long id);



}
