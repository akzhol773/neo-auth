package com.neobis.neoauth.repository;

import com.neobis.neoauth.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u WHERE (u.email = :emailOrUsername OR u.username = :emailOrUsername)")
    Optional<User> findByEmailOrUsername(@Param("emailOrUsername") String emailOrUsername);


}