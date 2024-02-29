package com.neobis.neoauth.repository;

import com.neobis.neoauth.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}