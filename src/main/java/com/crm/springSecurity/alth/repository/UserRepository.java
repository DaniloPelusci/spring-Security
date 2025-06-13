package com.crm.springSecurity.alth.repository;

import com.crm.springSecurity.alth.modelSecurity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    List<User> findByAuthoritiesDescription(String description);

}
