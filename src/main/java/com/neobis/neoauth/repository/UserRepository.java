package com.neobis.neoauth.repository;

import com.neobis.neoauth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}