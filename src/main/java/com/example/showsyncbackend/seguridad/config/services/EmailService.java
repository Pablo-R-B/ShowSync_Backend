package com.example.showsyncbackend.seguridad.config.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender javaMailSender;


    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    @Value("${app.url.base}")
    private String baseUrl;



    public void enviarCorreoVerificacion(String to, String token) {


        String subject = "Verificación de Email - ShowSync";
        String text = "¡Hola! Para verificar tu cuenta, por favor haz clic en el siguiente enlace:\n" +
                baseUrl + "/verificar-email?token=" + token;

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Error al enviar el correo de verificación a {}", to, e);
        }
    }
}
