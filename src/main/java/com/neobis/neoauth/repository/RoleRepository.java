package com.neobis.neoauth.repository;

import com.neobis.neoauth.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String roleUser);

    boolean existsByName(String name);
}