package edu.progavud.distrimusic.persona;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.progavud.distrimusic.email.EmailService;
import java.util.List;
import java.util.Map;

/**
 * Servicio que implementa la lógica de negocio relacionada con los usuarios.
 * 
 * Esta clase maneja todas las operaciones relacionadas con usuarios, incluyendo:
 * - Registro y autenticación de usuarios
 * - Gestión de perfiles
 * - Sistema de seguimiento entre usuarios
 * - Envío de emails de bienvenida
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final EmailService emailService;
    
    /**
     * Actualiza el perfil de un usuario existente.
     * Solo actualiza los campos que están presentes en el mapa de actualizaciones.
     *
     * @param usuario nombre de usuario a actualizar
     * @param updates mapa con los campos a actualizar
     * @return el usuario actualizado
     * @throws RuntimeException si el email ya está registrado por otro usuario
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
    
    /**
     * Registra un nuevo usuario en el sistema y envía un email de bienvenida.
     *
     * @param user datos del nuevo usuario
     * @return el usuario creado
     * @throws RuntimeException si el usuario o email ya existen
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
     * Autentica un usuario en el sistema.
     *
     * @param usuario nombre de usuario
     * @param contraseña contraseña del usuario
     * @return el usuario autenticado
     * @throws RuntimeException si el usuario no existe o la contraseña es incorrecta
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
     * Busca y retorna un usuario por su nombre de usuario.
     *
     * @param usuario nombre de usuario a buscar
     * @return el usuario encontrado
     * @throws RuntimeException si el usuario no existe
     */
    public UserEntity getUserByUsuario(String usuario) {
        return userRepository.findByUsuario(usuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    /**
     * Obtiene la lista de todos los usuarios registrados.
     *
     * @return lista de todos los usuarios
     */
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
    
    /**
     * Establece una relación de seguimiento entre dos usuarios.
     *
     * @param usuarioActual nombre del usuario que va a seguir
     * @param usuarioASeguir nombre del usuario a ser seguido
     * @throws RuntimeException si el usuario intenta seguirse a sí mismo o ya sigue al usuario
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
     * Elimina una relación de seguimiento entre dos usuarios.
     *
     * @param usuarioActual nombre del usuario que dejará de seguir
     * @param usuarioADejar nombre del usuario que será dejado de seguir
     * @throws RuntimeException si el usuario no sigue al usuario especificado
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
     * Obtiene la lista de seguidores de un usuario.
     *
     * @param usuario nombre del usuario
     * @return lista de usuarios que siguen al usuario especificado
     */
    public List<UserEntity> getSeguidores(String usuario) {
        return userRepository.findSeguidoresByUsuario(usuario);
    }
    
    /**
     * Obtiene la lista de usuarios que sigue un usuario.
     *
     * @param usuario nombre del usuario
     * @return lista de usuarios seguidos por el usuario especificado
     */
    public List<UserEntity> getSiguiendo(String usuario) {
        return userRepository.findSiguiendoByUsuario(usuario);
    }
    
    /**
     * Verifica si existe una relación de seguimiento entre dos usuarios.
     *
     * @param follower nombre del usuario seguidor
     * @param following nombre del usuario seguido
     * @return true si follower sigue a following
     */
    public boolean esSeguidor(String follower, String following) {
        return userRepository.isFollowing(follower, following);
    }
}