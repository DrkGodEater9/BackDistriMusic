package edu.progavud.distrimusic.persona;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.progavud.distrimusic.email.EmailService;
import edu.progavud.distrimusic.playlist.PlaylistEntity;
import edu.progavud.distrimusic.playlist.PlaylistRepository;
import edu.progavud.distrimusic.comment.CommentEntity;
import edu.progavud.distrimusic.comment.CommentRepository;
import java.util.List;
import java.util.Map;

/**
 * Servicio que implementa la lógica de negocio relacionada con los usuarios.
 * 
 * Esta clase maneja todas las operaciones relacionadas con usuarios,
 * incluyendo: - Registro y autenticación de usuarios - Gestión de perfiles -
 * Sistema de seguimiento entre usuarios - Envío de emails de bienvenida
 * - Eliminación completa de usuarios y sus datos relacionados
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
    private final PlaylistRepository playlistRepository;
    private final CommentRepository commentRepository;

    /**
     * Actualiza el perfil de un usuario existente. Solo actualiza los campos
     * que están presentes en el mapa de actualizaciones.
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
            if (nombre != null && !nombre.isBlank()) {
                user.setNombre(nombre);
            }
        }
        if (updates.containsKey("carrera")) {
            String carrera = (String) updates.get("carrera");
            user.setCarrera(carrera);
        }
        if (updates.containsKey("email")) {
            String email = (String) updates.get("email");
            if (email != null && !email.isBlank()) {
                if (!email.equals(user.getEmail()) && userRepository.existsByEmail(email)) {
                    throw new RuntimeException("El correo electrónico ya está registrado");
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
        if (userRepository.existsByUsuario(user.getUsuario())) {
            throw new RuntimeException("El nombre de usuario ya está registrado");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        UserEntity savedUser = userRepository.save(user);

        try {
            emailService.enviarEmailRegistro(
                    savedUser.getUsuario(),
                    savedUser.getNombre(),
                    savedUser.getEmail(),
                    savedUser.getCarrera(),
                    savedUser.getCodigoEstudiantil()
            );
        } catch (Exception e) {
            // Email sending failed, but user was created successfully
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

    /**
 * Elimina un usuario del sistema junto con todos sus datos relacionados.
 * 
 * Este método realiza una eliminación completa en cascada que incluye:
 * - Verificación de credenciales del usuario para seguridad
 * - Eliminación de todos los comentarios realizados por el usuario
 * - Eliminación de todas las playlists creadas por el usuario (y sus comentarios)
 * - Eliminación de todas las relaciones de seguimiento (seguidores y siguiendo)
 * - Eliminación final del registro del usuario
 * 
 * La operación es transaccional, garantizando que todos los datos se eliminen
 * de forma consistente o que la operación falle completamente sin cambios parciales.
 *
 * @param usuario nombre del usuario a eliminar
 * @param password contraseña del usuario para verificación de seguridad
 * @throws RuntimeException si el usuario no existe, la contraseña es incorrecta, 
 *                         o hay errores durante el proceso de eliminación
 */
@Transactional
public void deleteUser(String usuario, String password) {
    UserEntity user = userRepository.findByUsuario(usuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    if (!user.validarCredenciales(password)) {
        throw new RuntimeException("Contraseña incorrecta");
    }

    try {
        
        
        // 1. PRIMER PASO: Eliminar comentarios del usuario en TODAS las playlists
        
        try {
            List<CommentEntity> userComments = commentRepository.findByUsuarioUsuarioOrderByIdDesc(usuario);
            if (!userComments.isEmpty()) {
                
                commentRepository.deleteAll(userComments);
                commentRepository.flush();

            }
        } catch (Exception e) {
            
            // Continuar con el proceso
        }

        // 2. SEGUNDO PASO: Eliminar playlists del usuario (y sus comentarios asociados)
        
        try {
            List<PlaylistEntity> userPlaylists = playlistRepository.findByUsuarioUsuario(usuario);
            if (!userPlaylists.isEmpty()) {

                
                for (PlaylistEntity playlist : userPlaylists) {
                    
                    // Eliminar comentarios en esta playlist
                    List<CommentEntity> playlistComments = commentRepository.findByPlaylistId(playlist.getId());
                    if (!playlistComments.isEmpty()) {
                        commentRepository.deleteAll(playlistComments);
                    }
                    
                    // Limpiar relaciones de canciones
                    if (playlist.getCanciones() != null) {
                        playlist.getCanciones().clear();
                        playlistRepository.save(playlist);
                    }
                    
                    // Eliminar la playlist
                    playlistRepository.deleteById(playlist.getId());
                }
                playlistRepository.flush();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error eliminando playlists del usuario");
        }

        // 3. TERCER PASO: Eliminar relaciones de seguimiento
        try {
            // Eliminar de las listas de "siguiendo" de otros usuarios
            List<UserEntity> followers = userRepository.findSeguidoresByUsuario(usuario);
            for (UserEntity follower : followers) {
                follower.getSiguiendo().remove(user);
                userRepository.save(follower);
            }

            // Eliminar de las listas de "seguidores" de otros usuarios
            List<UserEntity> following = userRepository.findSiguiendoByUsuario(usuario);
            for (UserEntity followed : following) {
                followed.getSeguidores().remove(user);
                userRepository.save(followed);
            }

            // Limpiar las propias listas del usuario
            user.getSeguidores().clear();
            user.getSiguiendo().clear();
            userRepository.save(user);
            userRepository.flush();
            
        } catch (Exception e) {
            // Continuar con el proceso
        }

        // 4. CUARTO PASO: Eliminar el usuario
        userRepository.deleteById(user.getId());
        userRepository.flush();


    } catch (Exception e) {
        throw new RuntimeException("Error interno al eliminar el usuario: " + e.getMessage());
    }
}
}