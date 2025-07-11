package edu.progavud.distrimusic.persona;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Controlador REST para la gestión de usuarios.
 * 
 * Esta clase expone endpoints REST para todas las operaciones relacionadas con usuarios:
 * - Registro y autenticación
 * - Gestión de perfiles
 * - Sistema de seguimiento entre usuarios
 * - Eliminación completa de usuarios
 * 
 * Todos los endpoints están protegidos contra CORS y manejan apropiadamente los
 * errores retornando códigos HTTP adecuados.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Actualiza el perfil de un usuario.
     * Solo actualiza los campos proporcionados en el mapa de actualizaciones.
     *
     * @param usuario nombre del usuario a actualizar
     * @param updates mapa con los campos a actualizar
     * @return ResponseEntity con el usuario actualizado o error
     */
    @PutMapping("/{usuario}")
    public ResponseEntity<?> updateUserProfile(
            @PathVariable String usuario,
            @RequestBody Map<String, Object> updates) {
        try {
            UserEntity updated = userService.updateUserProfile(usuario, updates);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    
    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user datos del nuevo usuario
     * @return ResponseEntity con el usuario creado y status 201 (CREATED)
     */
    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@Valid @RequestBody UserEntity user) {
        UserEntity savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    
    /**
     * Autentica un usuario en el sistema.
     *
     * @param loginRequest credenciales del usuario
     * @return ResponseEntity con los datos del usuario autenticado
     */
    @PostMapping("/login")
    public ResponseEntity<UserEntity> loginUser(@RequestBody UserEntity loginRequest) {
        UserEntity user = userService.authenticateUser(loginRequest.getUsuario(), loginRequest.getContraseña());
        return ResponseEntity.ok(user);
    }
    
    /**
     * Obtiene los datos de un usuario específico.
     *
     * @param usuario nombre del usuario a buscar
     * @return ResponseEntity con los datos del usuario
     */
    @GetMapping("/{usuario}")
    public ResponseEntity<UserEntity> getUserByUsuario(@PathVariable String usuario) {
        UserEntity user = userService.getUserByUsuario(usuario);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Obtiene la lista de todos los usuarios registrados.
     *
     * @return ResponseEntity con la lista de usuarios
     */
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    /**
     * Establece una relación de seguimiento entre dos usuarios.
     * Acepta el follower tanto por query param como por body JSON.
     *
     * @param usuario usuario a seguir
     * @param follower usuario que sigue (opcional por query param)
     * @param body body JSON que puede contener el follower (opcional)
     * @return ResponseEntity con mensaje de confirmación
     */
    @PostMapping("/{usuario}/follow")
    public ResponseEntity<Map<String, String>> seguirUsuario(
            @PathVariable String usuario,
            @RequestParam(required = false) String follower,
            @RequestBody(required = false) Map<String, Object> body) {
        try {
            String followerUser = follower;
            if ((followerUser == null || followerUser.isBlank()) && body != null && body.get("follower") != null) {
                followerUser = String.valueOf(body.get("follower"));
            }
            if (followerUser == null || followerUser.isBlank()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "Falta el usuario que sigue",
                    "status", "error"
                ));
            }
            userService.seguirUsuario(followerUser, usuario);
            return ResponseEntity.ok(Map.of(
                "message", "Ahora sigues a " + usuario,
                "status", "success"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "status", "error"
            ));
        }
    }
    
    /**
     * Elimina una relación de seguimiento entre dos usuarios.
     *
     * @param usuario usuario a dejar de seguir
     * @param follower usuario que deja de seguir
     * @return ResponseEntity con mensaje de confirmación
     */
    @DeleteMapping("/{usuario}/follow")
    public ResponseEntity<Map<String, String>> dejarDeSeguir(
            @PathVariable String usuario,
            @RequestParam String follower) {
        try {
            userService.dejarDeSeguir(follower, usuario);
            return ResponseEntity.ok(Map.of(
                "message", "Dejaste de seguir a " + usuario,
                "status", "success"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "status", "error"
            ));
        }
    }
    
    /**
     * Obtiene la lista de seguidores de un usuario.
     *
     * @param usuario nombre del usuario
     * @return ResponseEntity con la lista de seguidores
     */
    @GetMapping("/{usuario}/followers")
    public ResponseEntity<List<UserEntity>> getSeguidores(@PathVariable String usuario) {
        List<UserEntity> seguidores = userService.getSeguidores(usuario);
        return ResponseEntity.ok(seguidores);
    }
    
    /**
     * Obtiene la lista de usuarios que sigue un usuario.
     *
     * @param usuario nombre del usuario
     * @return ResponseEntity con la lista de usuarios seguidos
     */
    @GetMapping("/{usuario}/following")
    public ResponseEntity<List<UserEntity>> getSiguiendo(@PathVariable String usuario) {
        List<UserEntity> siguiendo = userService.getSiguiendo(usuario);
        return ResponseEntity.ok(siguiendo);
    }
    
    /**
     * Verifica si existe una relación de seguimiento entre dos usuarios.
     *
     * @param usuario usuario potencialmente seguido
     * @param follower usuario potencialmente seguidor
     * @return ResponseEntity con un booleano indicando la relación
     */
    @GetMapping("/{usuario}/is-following")
    public ResponseEntity<Map<String, Boolean>> verificarSeguimiento(
            @PathVariable String usuario,
            @RequestParam String follower) {
        boolean isFollowing = userService.esSeguidor(follower, usuario);
        return ResponseEntity.ok(Map.of("isFollowing", isFollowing));
    }

    /**
     * Elimina un usuario del sistema junto con todos sus datos relacionados.
     * 
     * Este endpoint realiza una eliminación completa y permanente del usuario,
     * incluyendo todas sus playlists, comentarios y relaciones sociales.
     * 
     * Por seguridad, requiere la contraseña del usuario para confirmar la operación.
     * La eliminación es irreversible y se realiza de forma transaccional.
     * 
     * @param usuario nombre del usuario a eliminar
     * @param requestBody objeto JSON que debe contener la contraseña del usuario para verificación
     *                   Formato esperado: {"password": "contraseña_del_usuario"}
     * @return ResponseEntity con mensaje de confirmación (200) o error (400/404)
     *         - 200: Usuario eliminado exitosamente
     *         - 400: Contraseña faltante, incorrecta o error en el proceso
     *         - 404: Usuario no encontrado
     */
    @DeleteMapping("/{usuario}")
    public ResponseEntity<?> deleteUser(
            @PathVariable String usuario,
            @RequestBody Map<String, String> requestBody) {
        try {
            String password = requestBody.get("password");
            if (password == null || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                    "error", "La contraseña es requerida",
                    "status", "error"
                ));
            }
            
            userService.deleteUser(usuario, password);
            return ResponseEntity.ok(Map.of(
                "message", "Usuario eliminado exitosamente",
                "status", "success"
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", e.getMessage(),
                "status", "error"
            ));
        }
    }
}