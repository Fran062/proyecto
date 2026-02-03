package edu.serviClick.proyecto.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServicio {

    @Autowired
    private JavaMailSender mailSender;

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
}
