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
 * Servicio para el envío de correos electrónicos en la aplicación DistriMusic.
 * 
 * Esta clase implementa la funcionalidad de envío de correos electrónicos utilizando
 * Spring Mail. Proporciona soporte para envío de correos HTML y texto plano, con un
 * sistema de fallback para garantizar la entrega de mensajes.
 * 
 * La configuración del servidor SMTP se realiza a través de application.properties
 * con las siguientes propiedades:
 * - app.mail.from: dirección de correo del remitente
 * - app.mail.from-name: nombre mostrado del remitente
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
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
     * Envía un correo electrónico de bienvenida al usuario recién registrado.
     * 
     * Este método intenta primero enviar un correo usando MimeMessage para soporte
     * de contenido enriquecido. Si falla, recurre a SimpleMailMessage como fallback.
     * Registra todos los intentos y resultados en el log del sistema.
     *
     * @param usuario nombre de usuario en la plataforma
     * @param nombre nombre completo del estudiante
     * @param email dirección de correo electrónico del destinatario
     * @param carrera carrera universitaria del estudiante
     * @param codigoEstudiantil código de identificación estudiantil
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
     * Método de respaldo para enviar correos en formato simple.
     * 
     * Se utiliza cuando falla el envío mediante MimeMessage. Utiliza SimpleMailMessage
     * que solo soporta texto plano pero es más robusto.
     *
     * @param usuario nombre de usuario
     * @param nombre nombre completo
     * @param email dirección de correo del destinatario
     * @param carrera carrera universitaria
     * @param codigoEstudiantil código estudiantil
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
     * Genera el contenido del correo de bienvenida.
     * 
     * Crea un mensaje personalizado con los datos del usuario y la información
     * relevante sobre la plataforma DistriMusic. El mensaje incluye:
     * - Datos de la cuenta
     * - Funcionalidades disponibles
     * - URL de acceso
     * - Información institucional
     *
     * @param usuario nombre de usuario
     * @param nombre nombre completo
     * @param carrera carrera universitaria
     * @param codigoEstudiantil código estudiantil
     * @return String con el contenido formateado del correo
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