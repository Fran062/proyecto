package edu.serviClick.proyecto.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServicio {

    @Autowired
    private JavaMailSender mailSender;

    @org.springframework.beans.factory.annotation.Value("${app.frontend.url}")
    private String frontendUrl;

    public void enviarCodigoRecuperacion(String destinatario, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("serviclick.noreply@gmail.com");
        mensaje.setTo(destinatario);
        mensaje.setSubject("Código de Recuperación de Contraseña - ServiClick");
        mensaje.setText("Hola,\n\n" +
                "Has solicitado restablecer tu contraseña en ServiClick.\n" +
                "Tu código de recuperación es: " + codigo + "\n\n" +
                "Este código expirará en 15 minutos.\n" +
                "Si no has solicitado esto, ignora este mensaje.\n\n" +
                "El equipo de ServiClick");

        mailSender.send(mensaje);
        System.out.println("Correo de recuperación enviado a " + destinatario);
    }

    public void enviarConfirmacion(String destinatario, String token) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom("serviclick.noreply@gmail.com");
            helper.setTo(destinatario);
            helper.setSubject("Confirma tu cuenta - ServiClick");

            String htmlContent = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "    <meta charset='UTF-8'>" +
                    "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "</head>" +
                    "<body style='margin: 0; padding: 0; font-family: Arial, sans-serif; background-color: #f4f4f4;'>" +
                    "    <table width='100%' cellpadding='0' cellspacing='0' style='background-color: #f4f4f4; padding: 20px;'>"
                    +
                    "        <tr>" +
                    "            <td align='center'>" +
                    "                <table width='600' cellpadding='0' cellspacing='0' style='background-color: #ffffff; border-radius: 8px; overflow: hidden; box-shadow: 0 2px 4px rgba(0,0,0,0.1);'>"
                    +
                    "                    <!-- Header -->" +
                    "                    <tr>" +
                    "                        <td style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 40px 30px; text-align: center;'>"
                    +
                    "                            <h1 style='color: #ffffff; margin: 0; font-size: 28px;'>¡Bienvenido a ServiClick!</h1>"
                    +
                    "                        </td>" +
                    "                    </tr>" +
                    "                    <!-- Body -->" +
                    "                    <tr>" +
                    "                        <td style='padding: 40px 30px;'>" +
                    "                            <p style='color: #333333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;'>"
                    +
                    "                                Hola," +
                    "                            </p>" +
                    "                            <p style='color: #333333; font-size: 16px; line-height: 1.6; margin: 0 0 20px 0;'>"
                    +
                    "                                Gracias por registrarte en <strong>ServiClick</strong>. Estamos emocionados de tenerte con nosotros."
                    +
                    "                            </p>" +
                    "                            <p style='color: #333333; font-size: 16px; line-height: 1.6; margin: 0 0 30px 0;'>"
                    +
                    "                                Para activar tu cuenta y comenzar a usar nuestros servicios, por favor haz clic en el botón de abajo:"
                    +
                    "                            </p>" +
                    "                            <!-- Button -->" +
                    "                            <table width='100%' cellpadding='0' cellspacing='0'>" +
                    "                                <tr>" +
                    "                                    <td align='center' style='padding: 20px 0;'>" +
                    "                                        <a href='" + frontendUrl + "/confirmar?token=" + token
                    + "' " +
                    "                                           style='display: inline-block; padding: 16px 40px; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); "
                    +
                    "                                           color: #ffffff; text-decoration: none; border-radius: 6px; font-size: 16px; font-weight: bold; "
                    +
                    "                                           box-shadow: 0 4px 6px rgba(102, 126, 234, 0.4);'>" +
                    "                                            Activar mi cuenta" +
                    "                                        </a>" +
                    "                                    </td>" +
                    "                                </tr>" +
                    "                            </table>" +
                    "                            <p style='color: #666666; font-size: 14px; line-height: 1.6; margin: 30px 0 0 0;'>"
                    +
                    "                                Si no has creado una cuenta en ServiClick, puedes ignorar este mensaje."
                    +
                    "                            </p>" +
                    "                        </td>" +
                    "                    </tr>" +
                    "                    <!-- Footer -->" +
                    "                    <tr>" +
                    "                        <td style='background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;'>"
                    +
                    "                            <p style='color: #6c757d; font-size: 14px; margin: 0 0 10px 0;'>" +
                    "                                El equipo de ServiClick" +
                    "                            </p>" +
                    "                            <p style='color: #adb5bd; font-size: 12px; margin: 0;'>" +
                    "                                © 2025 ServiClick. Todos los derechos reservados." +
                    "                            </p>" +
                    "                        </td>" +
                    "                    </tr>" +
                    "                </table>" +
                    "            </td>" +
                    "        </tr>" +
                    "    </table>" +
                    "</body>" +
                    "</html>";

            helper.setText(htmlContent, true);
            mailSender.send(mensaje);
            System.out.println("Correo de confirmación HTML enviado a " + destinatario);
        } catch (Exception e) {
            System.err.println("Error enviando correo de confirmación: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
