package com.neobis.neoauth.dtos;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for {@link com.neobis.neoauth.entities.User}
 */
@Data
public record UserDto(String username, String password) implements Serializable {
}