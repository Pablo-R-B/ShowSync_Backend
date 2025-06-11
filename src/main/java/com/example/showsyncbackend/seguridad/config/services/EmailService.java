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
        String text = "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "   <meta charset='UTF-8'>" +
                "   <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                "</head>" +
                "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #08080C;'>" +
                "   <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #08080C; padding: 20px;'>" +
                "       <tr>" +
                "           <td align='center'>" +
                "               <table width='600' cellpadding='0' cellspacing='0' style='background-color: #1a1a1a; border-radius: 12px; padding: 30px; border: 1px solid #770316;'>" +
                "                   <tr>" +
                "                       <td align='center'>" +
                "                           <img src='cid:logo' alt='ShowSync Logo' style='max-width: 200px; margin-bottom: 25px;'>" +
                "                       </td>" +
                "                   </tr>" +
                "                   <tr>" +
                "                       <td align='center'>" +
                "                           <h2 style='color: #BF0D22; font-size: 28px; margin-bottom: 20px; font-weight: bold;'>¡Bienvenido a ShowSync!</h2>" +
                "                       </td>" +
                "                   </tr>" +
                "                   <tr>" +
                "                       <td style='color: #FBFAFA; font-size: 16px; line-height: 1.6; margin-bottom: 25px;'>" +
                "                           <p>¡Hola!</p>" +
                "                           <p style='color: #CCCBCB;'>Estamos encantados de tenerte a bordo. Para activar tu cuenta y empezar a explorar todo el talento y los eventos, por favor haz clic en el botón de abajo:</p>" +
                "                       </td>" +
                "                   </tr>" +
                "                   <tr>" +
                "                       <td align='center' style='padding: 20px 0;'>" +
                "                           <a href='" + baseUrl + "/verificar-email?token=" + token + "' " +
                "                              style='background-color: #BF0D22; color: white; padding: 15px 30px; " +
                "                              text-decoration: none; border-radius: 50px; font-weight: bold; font-size: 18px; display: inline-block;'>Verificar mi Cuenta</a>" +
                "                       </td>" +
                "                   </tr>" +
                "                   <tr>" +
                "                       <td style='color: #A0A4AD; font-size: 14px; text-align: center; padding-top: 30px;'>" +
                "                           <p>Si no solicitaste la creación de esta cuenta, por favor ignora este mensaje.</p>" +
                "                           <p style='font-size: 12px;'>&copy; " + java.time.Year.now().getValue() + " ShowSync. Todos los derechos reservados.</p>" +
                "                       </td>" +
                "                   </tr>" +
                "               </table>" +
                "           </td>" +
                "       </tr>" +
                "   </table>" +
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
