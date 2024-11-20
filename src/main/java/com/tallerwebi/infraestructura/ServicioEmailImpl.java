package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.ServicioEmail;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ServicioEmailImpl implements ServicioEmail {
    private final JavaMailSender mailSender;

    @Autowired
    public ServicioEmailImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true); // 'true' para indicar que es HTML
        helper.setFrom("trucotecaoficialgod@gmail.com");

        mailSender.send(mimeMessage);
    }

    @Override
    public String generateValidationCode() {
        // Puedes usar una librería para generar un código aleatorio de 6 dígitos
        return String.format("%06d", (int)(Math.random() * 1000000));
    }

    @Override
    public void sendValidationCode(String to, String validationCode) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(to);
        helper.setSubject("Código de Validación");
        helper.setText("Tu código de validación es: " + validationCode);
        helper.setFrom("trucotecaoficialgod@gmail.com");

        mailSender.send(mimeMessage);
    }
}