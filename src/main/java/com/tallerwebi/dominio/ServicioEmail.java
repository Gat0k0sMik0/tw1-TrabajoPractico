package com.tallerwebi.dominio;

import jakarta.mail.MessagingException;

public interface ServicioEmail {
    void sendEmail(String to, String subject, String text) throws MessagingException;
    String generateValidationCode();
    void sendValidationCode(String to, String validationCode) throws MessagingException;
}
