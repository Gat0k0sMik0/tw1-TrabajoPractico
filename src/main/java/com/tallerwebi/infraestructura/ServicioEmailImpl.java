package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.ServicioEmail;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class ServicioEmailImpl implements ServicioEmail {
    private static final String EMAIL_FROM = "trucotecaoficialgod@gmail.com";
    private static final String APP_PASSWORD = "notp klss fgfu xfei";

    @Override
    public void sendEmail(String email_to, String subject, String htmlContent) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(EMAIL_FROM));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email_to));
        message.setSubject(subject);
        message.setText(htmlContent);
        Transport.send(message);
    }

    @Override
    public String generateValidationCode() {
        // Genera un código de validación aleatorio de 6 dígitos
        return String.format("%06d", (int)(Math.random() * 1000000));
    }

    @Override
    public void sendValidationCode(String email_to, String validationCode) throws MessagingException {
        String subject = "Código de Validación";
        String htmlContent = "Tu código de validación es: " + validationCode;

        // Reutilizamos el método sendEmail para enviar el código
        sendEmail(email_to, subject, htmlContent);
    }

    private static Session getEmailSession() {
        return Session.getInstance(getGmailProperties(), new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, APP_PASSWORD);
            }
        });
    }

    private static Properties getGmailProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        return prop;
    }
}