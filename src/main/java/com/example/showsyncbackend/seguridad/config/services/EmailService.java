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

    @Value("${app.url.base:http://localhost:8081}")
    private String baseUrl;

    @Value("${app.url.frontend:http://localhost:8081}")
    private String frontendUrl;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void enviarCorreoVerificacion(String to, String token) {
        String subject = "Verificación de Email - ShowSync";
        String verificationUrl = frontendUrl + "/verificar-email?token=" + token;
        String logoUrl = baseUrl + "/logo_showsync_fondonegro.png";

        String htmlContent = buildVerificationEmailHtml(verificationUrl, logoUrl);

        sendEmail(to, subject, htmlContent);
    }

    private String buildVerificationEmailHtml(String verificationUrl, String logoUrl) {
        return "<!DOCTYPE html>" +
                "<html lang='es'>" +
                "<head>" +
                "   <meta charset='UTF-8'>" +
                "   <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "   <title>Verificación de Email</title>" +
                "   <style>" +
                "       body { font-family: Arial, sans-serif; line-height: 1.6; color: #333; max-width: 600px; margin: 0 auto; padding: 20px; }" +
                "       .logo { text-align: center; margin-bottom: 20px; }" +
                "       .logo img { max-width: 200px; height: auto; }" +
                "       .button { display: inline-block; background-color: #BF0D22; color: white; " +
                "                padding: 12px 24px; text-decoration: none; border-radius: 4px; " +
                "                font-weight: bold; margin: 20px 0; }" +
                "       .code { background: #f4f4f4; padding: 10px; border-radius: 4px; " +
                "              word-break: break-all; font-family: monospace; }" +
                "       .footer { margin-top: 30px; font-size: 12px; color: #777; }" +
                "   </style>" +
                "</head>" +
                "<body>" +
                "   <div class='logo'>" +
                "       <img src='" + logoUrl + "' alt='ShowSync Logo'>" +
                "   </div>" +
                "   <h2 style='text-align: center;'>¡Gracias por registrarte!</h2>" +
                "   <p>Para completar tu registro y verificar tu cuenta, haz clic en el siguiente botón:</p>" +
                "   <div style='text-align: center;'>" +
                "       <a href='" + verificationUrl + "' class='button'>Verificar mi cuenta</a>" +
                "   </div>" +
                "   <p>Si el botón no funciona, copia y pega la siguiente URL en tu navegador:</p>" +
                "   <p class='code'>" + verificationUrl + "</p>" +
                "   <p>Este enlace expirará en 24 horas.</p>" +
                "   <div class='footer'>" +
                "       <p>Si no solicitaste este registro, por favor ignora este mensaje.</p>" +
                "       <p>© 2025 ShowSync. Todos los derechos reservados.</p>" +
                "   </div>" +
                "</body>" +
                "</html>";
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            logger.info("Correo de verificación enviado a: {}", to);
        } catch (MessagingException e) {
            logger.error("Error al enviar correo a {}: {}", to, e.getMessage());
            throw new RuntimeException("Error al enviar correo de verificación", e);
        }
    }

    // Método adicional para enviar otros tipos de correos
    public void enviarCorreo(String to, String subject, String htmlContent) {
        sendEmail(to, subject, htmlContent);
    }
}