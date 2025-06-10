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

    @Value("${app.url.frontend}")
    private String frontendUrl;



    public void enviarCorreoVerificacion(String to, String token) {
        String subject = "Verificación de Email - ShowSync";

        // URL del logo (puedes alojarlo en tu servidor o usar un enlace público)
        String logoUrl = baseUrl + "/logo_showsync_fondonegro.png";

        // Plantilla HTML para el correo
        String text = "<html>" +
                "<body style='font-family: Arial, sans-serif; text-align: center; background-color: #08080C; margin: 0; padding: 20px;'>" +
                "   <div style='max-width: 600px; margin: 20px auto; background-color: #1a1a1a; padding: 30px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0, 0, 0, 0.5); border: 1px solid #770316;'>" +
                "       <img src='" + logoUrl + "' alt='ShowSync Logo' style='max-width: 200px; margin-bottom: 25px; display: block; margin-left: auto; margin-right: auto;'>" +
                "       <h2 style='color: #BF0D22; font-size: 28px; margin-bottom: 20px; font-weight: bold;'>¡Bienvenido a ShowSync!</h2>" +
                "       <p style='color: #FBFAFA; font-size: 16px; line-height: 1.6; margin-bottom: 25px;'>¡Hola!</p>" +
                "       <p style='color: #CCCBCB; font-size: 16px; line-height: 1.6; margin-bottom: 30px;'>Estamos encantados de tenerte a bordo. Para activar tu cuenta y empezar a explorar todo el talento y los eventos, por favor haz clic en el botón de abajo:</p>" +
                "       <p style='margin-bottom: 30px;'>" +
                "           <a href='" + baseUrl + "/verificar-email?token=" + token + "' " +
                "              style='background-color: #BF0D22; color: white; padding: 15px 30px; " +
                "              text-decoration: none; border-radius: 50px; font-weight: bold; font-size: 18px; " +
                "              transition: background-color 0.3s ease, transform 0.2s ease; display: inline-block;' " +
                "              onmouseover=\"this.style.backgroundColor='#770316'; this.style.transform='scale(1.05)';\" " +
                "              onmouseout=\"this.style.backgroundColor='#BF0D22'; this.style.transform='scale(1)';\">Verificar mi Cuenta</a></p>" +
                "       <p style='color: #A0A4AD; font-size: 14px; margin-top: 30px;'>" +
                "           Si no solicitaste la creación de esta cuenta, por favor ignora este mensaje." +
                "       </p>" +
                "       <p style='color: #A0A4AD; font-size: 12px; margin-top: 15px;'>&copy; " + java.time.Year.now().getValue() + " ShowSync. Todos los derechos reservados.</p>" +
                "   </div>" +
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

    public void enviarCorreoRecuperacion(String to, String token) {
        String subject = "Recuperación de Contraseña - ShowSync";
        String recoveryUrl = frontendUrl + "/auth/restablecer?token=" + token;


        String htmlContent = buildRecoveryEmailHtml(recoveryUrl);
        sendEmail(to, subject, htmlContent);
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

    private String buildRecoveryEmailHtml(String recoveryUrl) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><style>...</style></head>" +
                "<body>" +
                "   <h2>Restablece tu contraseña</h2>" +
                "   <p>Haz clic en el siguiente enlace para restablecer tu contraseña:</p>" +
                "   <a href='" + recoveryUrl + "'>Restablecer contraseña</a>" +
                "   <p>Si no solicitaste esto, ignora este correo.</p>" +
                "</body>" +
                "</html>";
    }

}
