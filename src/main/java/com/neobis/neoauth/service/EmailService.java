package com.neobis.neoauth.service;




public interface EmailService {
    void sendConfirm(String to, String email);
    void sendReset(String to, String email);
}
