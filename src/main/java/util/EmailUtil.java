package util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil {
    
    /**
     * Método para enviar correo de notificación de registro
     */
    public static boolean enviarCorreoRegistro(String destinatario, String nombreUsuario) {
        try {
            // Configuración del servidor SMTP (Gmail)
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            
            // ✅ CREDENCIALES UNIFICADAS - USA LA MISMA EN AMBOS MÉTODOS
            final String usuario = "jedaojeda223@umariana.edu.co";
            final String password = "ysmj cewd utxr nsel"; // ⚠️ CAMBIAR ESTO
            
            // Crear sesión con autenticación
            Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usuario, password);
                    }
                });
            
            // Crear mensaje de correo
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, 
                InternetAddress.parse(destinatario));
            message.setSubject("🎉 Registro Exitoso - Sistema de Autenticación UMariana");
            
            // Contenido HTML del correo
            String contenidoHTML = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }"
                + ".container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }"
                + ".header { background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); color: white; padding: 20px; text-align: center; border-radius: 8px; }"
                + ".content { padding: 20px; }"
                + ".footer { margin-top: 20px; padding: 15px; background: #f8f9fa; border-radius: 5px; text-align: center; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h2>¡Bienvenido al Sistema de Autenticación!</h2>"
                + "</div>"
                + "<div class='content'>"
                + "<h3>Hola " + nombreUsuario + ",</h3>"
                + "<p>Tu registro en el <strong>Sistema de Autenticación UMariana</strong> ha sido exitoso.</p>"
                + "<p>Ahora puedes acceder al sistema con las siguientes credenciales:</p>"
                + "<ul>"
                + "<li><strong>Correo:</strong> " + destinatario + "</li>"
                + "<li><strong>Contraseña:</strong> La que ingresaste durante el registro</li>"
                + "</ul>"
                + "<p>Puedes iniciar sesión en: <a href='http://localhost:8080/SistemaAutenticacion/'>Sistema de Autenticación</a></p>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>Este es un mensaje automático, por favor no respondas a este correo.</p>"
                + "<p><small>Sistema de Autenticación UMariana</small></p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";
            
            message.setContent(contenidoHTML, "text/html; charset=utf-8");
            
            // Enviar mensaje
            Transport.send(message);
            System.out.println("✅ Correo enviado exitosamente a: " + destinatario);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("❌ Error enviando correo: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Método para enviar correo de notificación de acceso
     */
    public static boolean enviarCorreoAcceso(String destinatario, String nombreUsuario, String ipAddress) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");
            props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            
            // ✅ MISMAS CREDENCIALES QUE EL MÉTODO DE REGISTRO
            final String usuario = "jedaojeda223@umariana.edu.co";
            final String password = "ysmj cewd utxr nsel"; // ⚠️ CAMBIAR ESTO
            
            Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(usuario, password);
                    }
                });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(usuario));
            message.setRecipients(Message.RecipientType.TO, 
                InternetAddress.parse(destinatario));
            message.setSubject("🔐 Nuevo Acceso Detectado - Sistema UMariana");
            
            String contenidoHTML = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }"
                + ".container { background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }"
                + ".header { background: linear-gradient(135deg, #28a745 0%, #20c997 100%); color: white; padding: 20px; text-align: center; border-radius: 8px; }"
                + ".warning { background: #fff3cd; border: 1px solid #ffeaa7; padding: 15px; border-radius: 5px; margin: 15px 0; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'>"
                + "<h2>Nuevo Acceso al Sistema</h2>"
                + "</div>"
                + "<h3>Hola " + nombreUsuario + ",</h3>"
                + "<p>Se ha detectado un nuevo acceso a tu cuenta en el <strong>Sistema de Autenticación UMariana</strong>.</p>"
                + "<div class='warning'>"
                + "<h4>📋 Detalles del Acceso:</h4>"
                + "<ul>"
                + "<li><strong>Usuario:</strong> " + nombreUsuario + "</li>"
                + "<li><strong>Dirección IP:</strong> " + ipAddress + "</li>"
                + "<li><strong>Fecha/Hora:</strong> " + java.time.LocalDateTime.now() + "</li>"
                + "</ul>"
                + "</div>"
                + "<p>Si reconoces esta actividad, puedes ignorar este mensaje.</p>"
                + "<p>Si NO reconoces este acceso, por favor cambia tu contraseña inmediatamente.</p>"
                + "<br>"
                + "<p><small>Sistema de Autenticación UMariana - Notificación Automática</small></p>"
                + "</div>"
                + "</body>"
                + "</html>";
            
            message.setContent(contenidoHTML, "text/html; charset=utf-8");
            Transport.send(message);
            System.out.println("✅ Correo de acceso enviado a: " + destinatario);
            return true;
            
        } catch (MessagingException e) {
            System.err.println("❌ Error enviando correo de acceso: " + e.getMessage());
            return false;
        }
    }
}