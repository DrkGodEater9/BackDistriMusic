package edu.progavud.distrimusic.persona;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@Valid @RequestBody UserEntity user) {
        UserEntity savedUser = userService.registerUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserEntity> loginUser(@RequestBody UserEntity loginRequest) {
        UserEntity user = userService.authenticateUser(loginRequest.getUsuario(), loginRequest.getContraseña());
        return ResponseEntity.ok(user);
    }
    
    @GetMapping("/{usuario}")
    public ResponseEntity<UserEntity> getUserByUsuario(@PathVariable String usuario) {
        UserEntity user = userService.getUserByUsuario(usuario);
        return ResponseEntity.ok(user);
    }
    
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        List<UserEntity> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // ✅ Endpoints para sistema de seguimiento (solo los básicos)
    
    /**
     * Seguir a un usuario
     * POST /api/users/{usuario}/follow?follower=usuarioActual
     */
    @PostMapping("/{usuario}/follow")
    public ResponseEntity<Map<String, String>> seguirUsuario(
            @PathVariable String usuario,
            @RequestParam String follower) {
        try {
            userService.seguirUsuario(follower, usuario);
            return ResponseEntity.ok(Map.of(
                "message", "Ahora sigues a " + usuario,
                "action", "follow",
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
     * Dejar de seguir a un usuario
     * DELETE /api/users/{usuario}/follow?follower=usuarioActual
     */
    @DeleteMapping("/{usuario}/follow")
    public ResponseEntity<Map<String, String>> dejarDeSeguir(
            @PathVariable String usuario,
            @RequestParam String follower) {
        try {
            userService.dejarDeSeguir(follower, usuario);
            return ResponseEntity.ok(Map.of(
                "message", "Dejaste de seguir a " + usuario,
                "action", "unfollow",
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
     * Obtener seguidores de un usuario
     * GET /api/users/{usuario}/followers
     */
    @GetMapping("/{usuario}/followers")
    public ResponseEntity<List<UserEntity>> getSeguidores(@PathVariable String usuario) {
        List<UserEntity> seguidores = userService.getSeguidores(usuario);
        return ResponseEntity.ok(seguidores);
    }
    
    /**
     * Obtener usuarios que sigue un usuario
     * GET /api/users/{usuario}/following
     */
    @GetMapping("/{usuario}/following")
    public ResponseEntity<List<UserEntity>> getSiguiendo(@PathVariable String usuario) {
        List<UserEntity> siguiendo = userService.getSiguiendo(usuario);
        return ResponseEntity.ok(siguiendo);
    }
    
    /**
     * Verificar si un usuario sigue a otro
     * GET /api/users/{usuario}/is-following?follower=usuarioActual
     */
    @GetMapping("/{usuario}/is-following")
    public ResponseEntity<Map<String, Boolean>> verificarSeguimiento(
            @PathVariable String usuario,
            @RequestParam String follower) {
        boolean isFollowing = userService.esSeguidor(follower, usuario);
        return ResponseEntity.ok(Map.of("isFollowing", isFollowing));
    }
}