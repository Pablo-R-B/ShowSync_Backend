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

        // URL del logo (puedes alojarlo en tu servidor o usar un enlace público)
        String logoUrl = baseUrl + "/logo_showsync_fondonegro.png";

        // Plantilla HTML para el correo
        String text = "<html>" +
                "<body style='font-family: Arial, sans-serif; text-align: center;'>" +
                "   <img src='" + logoUrl + "' alt='ShowSync Logo' style='max-width: 200px;'>" +
                "   <h2 style='color: #333;'>¡Hola!</h2>" +
                "   <p>Para verificar tu cuenta, por favor haz clic en el siguiente enlace:</p>" +
                "   <p><a href='" + baseUrl + "/verificar-email?token=" + token + "' " +
                "      style='background-color: #BF0D22; color: white; padding: 10px 15px; " +
                "      text-decoration: none; border-radius: 5px;' " +
                "      onmouseover=\"this.style.backgroundColor='#A0A4AD'\" " +
                "      onmouseout=\"this.style.backgroundColor='#BF0D22'\">Verificar Cuenta</a></p>" +
                "   <p>Si no solicitaste esto, ignora este mensaje.</p>" +
                "</body>" +
                "</html>";

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text, true); // Habilitar HTML

            javaMailSender.send(message);
        } catch (MessagingException e) {
            logger.error("Error al enviar el correo de verificación a {}", to, e);
        }
    }

}
