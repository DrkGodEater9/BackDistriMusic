package edu.progavud.distrimusic.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de comentarios en playlists.
 * 
 * Esta clase maneja todas las operaciones CRUD relacionadas con los comentarios,
 * incluyendo la creación, lectura, actualización y eliminación de comentarios en playlists.
 * 
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {
    
    private final CommentService commentService;
    
    /**
     * Crea un nuevo comentario en una playlist.
     * 
     * @param playlistId ID de la playlist donde se creará el comentario
     * @param usuario Nombre de usuario del autor del comentario
     * @param comment Entidad del comentario con el contenido
     * @return ResponseEntity con el comentario creado y status 201 (CREATED)
     * @throws org.springframework.web.server.ResponseStatusException con estado 404 si la playlist no existe
     * @throws org.springframework.web.server.ResponseStatusException con estado 403 si la playlist es privada
     */
    @PostMapping("/playlist/{playlistId}")
    public ResponseEntity<CommentEntity> createComment(
            @PathVariable Long playlistId,
            @RequestParam String usuario,
            @Valid @RequestBody CommentEntity comment) {
        CommentEntity savedComment = commentService.createComment(playlistId, usuario, comment.getContenido());
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }
    
    /**
     * Obtiene un comentario específico por su ID.
     * 
     * @param id ID del comentario a buscar
     * @return ResponseEntity con el comentario encontrado
     * @throws org.springframework.web.server.ResponseStatusException con estado 404 si el comentario no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommentEntity> getCommentById(@PathVariable Long id) {
        CommentEntity comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }
    
    /**
     * Obtiene todos los comentarios de una playlist específica.
     * 
     * @param playlistId ID de la playlist de la cual obtener los comentarios
     * @return ResponseEntity con la lista de comentarios de la playlist
     * @throws org.springframework.web.server.ResponseStatusException con estado 404 si la playlist no existe
     * @throws org.springframework.web.server.ResponseStatusException con estado 403 si la playlist es privada
     */
    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<List<CommentEntity>> getCommentsByPlaylist(@PathVariable Long playlistId) {
        List<CommentEntity> comments = commentService.getCommentsByPlaylist(playlistId);
        return ResponseEntity.ok(comments);
    }
    
    /**
     * Obtiene todos los comentarios realizados por un usuario específico.
     * 
     * @param usuario Nombre de usuario del cual obtener los comentarios
     * @return ResponseEntity con la lista de comentarios del usuario
     * @throws org.springframework.web.server.ResponseStatusException con estado 404 si el usuario no existe
     */
    @GetMapping("/user/{usuario}")
    public ResponseEntity<List<CommentEntity>> getCommentsByUser(@PathVariable String usuario) {
        List<CommentEntity> comments = commentService.getCommentsByUser(usuario);
        return ResponseEntity.ok(comments);
    }
    
    /**
     * Actualiza el contenido de un comentario existente.
     * 
     * @param id ID del comentario a actualizar
     * @param usuario Nombre de usuario del autor del comentario (para verificación)
     * @param comment Entidad del comentario con el nuevo contenido
     * @return ResponseEntity con el comentario actualizado
     * @throws org.springframework.web.server.ResponseStatusException con estado 404 si el comentario no existe
     * @throws org.springframework.web.server.ResponseStatusException con estado 403 si el usuario no es el autor
     */
    @PutMapping("/{id}")
    public ResponseEntity<CommentEntity> updateComment(
            @PathVariable Long id,
            @RequestParam String usuario,
            @Valid @RequestBody CommentEntity comment) {
        CommentEntity updatedComment = commentService.updateComment(id, usuario, comment.getContenido());
        return ResponseEntity.ok(updatedComment);
    }
    
    /**
     * Elimina un comentario específico.
     * 
     * @param id ID del comentario a eliminar
     * @param usuario Nombre de usuario del autor del comentario o dueño de la playlist
     * @return ResponseEntity sin contenido y status 204 (NO_CONTENT)
     * @throws org.springframework.web.server.ResponseStatusException con estado 404 si el comentario no existe
     * @throws org.springframework.web.server.ResponseStatusException con estado 403 si el usuario no tiene permiso
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestParam String usuario) {
        commentService.deleteComment(id, usuario);
        return ResponseEntity.noContent().build();
    }
}