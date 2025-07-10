package edu.progavud.distrimusic.persona;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.progavud.distrimusic.email.EmailService;
import java.util.List;
import java.util.Map;

/**
 * Servicio para gestión de usuarios con email de registro y seguimiento
 */
@Service
@RequiredArgsConstructor
public class UserService {
    /**
     * Actualiza el perfil del usuario (solo los campos enviados)
     */
    public UserEntity updateUserProfile(String usuario, Map<String, Object> updates) {
        UserEntity user = getUserByUsuario(usuario);

        if (updates.containsKey("nombre")) {
            String nombre = (String) updates.get("nombre");
            if (nombre != null && !nombre.isBlank()) user.setNombre(nombre);
        }
        if (updates.containsKey("carrera")) {
            String carrera = (String) updates.get("carrera");
            user.setCarrera(carrera);
        }
        if (updates.containsKey("email")) {
            String email = (String) updates.get("email");
            if (email != null && !email.isBlank()) {
                if (!email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
                    throw new RuntimeException("El email ya está registrado");
                }
                user.setEmail(email);
            }
        }
        if (updates.containsKey("profileImageUrl")) {
            String profileImageUrl = (String) updates.get("profileImageUrl");
            user.setProfileImageUrl(profileImageUrl);
        }
        if (updates.containsKey("newPassword")) {
            String newPassword = (String) updates.get("newPassword");
            if (newPassword != null && !newPassword.isBlank()) {
                user.setPassword(newPassword);
            }
        }
        return userRepository.save(user);
    }
    
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    /**
     * Registra un nuevo usuario y envía email de confirmación
     */
    public UserEntity registerUser(UserEntity user) {
        // Validar que el usuario no exista
        if (userRepository.existsByUsuario(user.getUsuario())) {
            throw new RuntimeException("El usuario ya existe");
        }
        
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }
        
        // Guardar usuario
        UserEntity savedUser = userRepository.save(user);
        
        // ENVIAR EMAIL DE REGISTRO
        try {
            emailService.enviarEmailRegistro(
                savedUser.getUsuario(),
                savedUser.getNombre(),
                savedUser.getEmail(),
                savedUser.getCarrera(),
                savedUser.getCodigoEstudiantil()
            );
        } catch (Exception e) {
            System.err.println("⚠️ Error enviando email: " + e.getMessage());
        }
        
        return savedUser;
    }
    
    /**
     * Autentica un usuario con credenciales
     */
    public UserEntity authenticateUser(String usuario, String contraseña) {
        UserEntity user = userRepository.findByUsuario(usuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!user.validarCredenciales(contraseña)) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        
        return user;
    }
    
    /**
     * Obtiene un usuario por su nombre de usuario
     */
    public UserEntity getUserByUsuario(String usuario) {
        return userRepository.findByUsuario(usuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    /**
     * Obtiene todos los usuarios
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    
    // ✅ Métodos para sistema de seguimiento
    
    /**
     * Seguir a un usuario
     */
    public void seguirUsuario(String usuarioActual, String usuarioASeguir) {
        if (usuarioActual.equals(usuarioASeguir)) {
            throw new RuntimeException("No puedes seguirte a ti mismo");
        }
        
        UserEntity follower = getUserByUsuario(usuarioActual);
        UserEntity following = getUserByUsuario(usuarioASeguir);
        
        if (follower.estaSiguiendo(following)) {
            throw new RuntimeException("Ya sigues a este usuario");
        }
        
        follower.getSiguiendo().add(following);
        following.getSeguidores().add(follower);
        
        userRepository.save(follower);
        userRepository.save(following);
    }
    
    /**
     * Dejar de seguir a un usuario
     */
    public void dejarDeSeguir(String usuarioActual, String usuarioADejar) {
        UserEntity follower = getUserByUsuario(usuarioActual);
        UserEntity following = getUserByUsuario(usuarioADejar);
        
        if (!follower.estaSiguiendo(following)) {
            throw new RuntimeException("No sigues a este usuario");
        }
        
        follower.getSiguiendo().remove(following);
        following.getSeguidores().remove(follower);
        
        userRepository.save(follower);
        userRepository.save(following);
    }
    
    /**
     * Obtener seguidores de un usuario
     */
    public List<UserEntity> getSeguidores(String usuario) {
        return userRepository.findSeguidoresByUsuario(usuario);
    }
    
    /**
     * Obtener usuarios que sigue un usuario
     */
    public List<UserEntity> getSiguiendo(String usuario) {
        return userRepository.findSiguiendoByUsuario(usuario);
    }
    
    /**
     * Verificar si un usuario sigue a otro
     */
    public boolean esSeguidor(String follower, String following) {
        return userRepository.isFollowing(follower, following);
    }
}