package edu.progavud.distrimusic.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Servicio para envío REAL de correos electrónicos
 * Cumple con el requerimiento del examen de enviar email al registrarse
 * @author Tu nombre
 * @version 1.0
 */
@Service
@Slf4j
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${app.mail.from}")
    private String fromEmail;
    
    @Value("${app.mail.from-name}")
    private String fromName;
    
    /**
     * Envía email REAL de registro al usuario
     * @param usuario nombre de usuario
     * @param nombre nombre completo
     * @param email email del usuario (donde llegará el correo)
     * @param carrera carrera del estudiante
     * @param codigoEstudiantil código estudiantil
     */
    public void enviarEmailRegistro(String usuario, String nombre, String email, String carrera, String codigoEstudiantil) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            
            // Configurar remitente con nombre personalizado
            helper.setFrom(fromEmail, fromName);
            helper.setTo(email);
            helper.setSubject("¡Bienvenido a DistriMusic! 🎵 - Tu cuenta ha sido creada");
            
            // Crear contenido del email
            String contenido = crearContenidoEmail(usuario, nombre, carrera, codigoEstudiantil);
            helper.setText(contenido, false); // false = texto plano
            
            // ✅ ENVIAR EMAIL REAL
            mailSender.send(mimeMessage);
            
            // Log de confirmación
            log.info("✅ EMAIL ENVIADO EXITOSAMENTE");
            log.info("From: {} <{}>", fromName, fromEmail);
            log.info("To: {} <{}>", nombre, email);
            log.info("Subject: ¡Bienvenido a DistriMusic!");
            log.info("Fecha: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            log.info("REQUERIMIENTO DEL EXAMEN CUMPLIDO: Email enviado al registrar usuario");
            
        } catch (Exception e) {
            log.error("ERROR enviando email: {}", e.getMessage());
            log.error("Verifique la configuración SMTP en application.properties");
            log.error("Username: {}", fromEmail);
            log.error("From Name: {}", fromName);
            log.error("Destinatario: {}", email);
            
            // Fallback: usar SimpleMailMessage
            enviarEmailSimple(usuario, nombre, email, carrera, codigoEstudiantil);
        }
    }
    
    /**
     * Fallback con SimpleMailMessage si falla MimeMessage
     */
    private void enviarEmailSimple(String usuario, String nombre, String email, String carrera, String codigoEstudiantil) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("¡Bienvenido a DistriMusic! - Tu cuenta ha sido creada");
            message.setText(crearContenidoEmail(usuario, nombre, carrera, codigoEstudiantil));
            
            mailSender.send(message);
            log.info("EMAIL SIMPLE ENVIADO como fallback");
            
        } catch (Exception e2) {
            log.error(" ERROR también con email simple: {}", e2.getMessage());
            log.info(" CONTENIDO QUE SE HABRÍA ENVIADO:");
            log.info(crearContenidoEmail(usuario, nombre, carrera, codigoEstudiantil));
        }
    }
    
    /**
     * Crea el contenido del email de registro
     */
    private String crearContenidoEmail(String usuario, String nombre, String carrera, String codigoEstudiantil) {
        return String.format("""
            ¡Hola %s! 👋
            
            ¡Bienvenido a DistriMusic! 🎵
            La plataforma musical de la Universidad Distrital Francisco José de Caldas
            
            Tu cuenta ha sido creada exitosamente:
            
            📋 DATOS DE TU CUENTA:
            👤 Usuario: %s
            🎓 Carrera: %s
            📝 Código Estudiantil: %s
            📅 Fecha de registro: %s
            
            🎵 ¿QUÉ PUEDES HACER EN DISTRIMUSIC?
            • Crear playlists públicas y privadas
            • Comentar en playlists de otros estudiantes
            • Dar likes a canciones y playlists
            • Descubrir música de la comunidad universitaria
            • Conectar con estudiantes de tu carrera
            
            🚀 ¡EMPIEZA AHORA!
            Accede en: http://localhost:8090
            Usuario: %s
            
            ¡Disfruta de la música estudiantil! 🎼
            
            --
            El equipo de DistriMusic
            Universidad Distrital Francisco José de Caldas
            Facultad de Ingeniería - Ingeniería de Sistemas
            
            Este es un correo automático generado por el sistema académico.
            """, 
            nombre, usuario, carrera, codigoEstudiantil,
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
            usuario
        );
    }
    
}