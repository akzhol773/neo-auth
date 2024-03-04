package com.neobis.neoauth.service;

import com.neobis.neoauth.entities.ConfirmationToken;
import com.neobis.neoauth.entities.PasswordResetToken;
import com.neobis.neoauth.entities.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface ResetTokenService {
    void saveResetToken(PasswordResetToken token);

    Optional<PasswordResetToken> getToken(String token);


}
