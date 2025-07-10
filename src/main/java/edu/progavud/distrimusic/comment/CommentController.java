package edu.progavud.distrimusic.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CommentController {
    
    private final CommentService commentService;
    
    @PostMapping("/playlist/{playlistId}")
    public ResponseEntity<CommentEntity> createComment(
            @PathVariable Long playlistId,
            @RequestParam String usuario,
            @Valid @RequestBody CommentEntity comment) {
        CommentEntity savedComment = commentService.createComment(playlistId, usuario, comment.getContenido());
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CommentEntity> getCommentById(@PathVariable Long id) {
        CommentEntity comment = commentService.getCommentById(id);
        return ResponseEntity.ok(comment);
    }
    
    @GetMapping("/playlist/{playlistId}")
    public ResponseEntity<List<CommentEntity>> getCommentsByPlaylist(@PathVariable Long playlistId) {
        List<CommentEntity> comments = commentService.getCommentsByPlaylist(playlistId);
        return ResponseEntity.ok(comments);
    }
    
    @GetMapping("/user/{usuario}")
    public ResponseEntity<List<CommentEntity>> getCommentsByUser(@PathVariable String usuario) {
        List<CommentEntity> comments = commentService.getCommentsByUser(usuario);
        return ResponseEntity.ok(comments);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CommentEntity> updateComment(
            @PathVariable Long id,
            @RequestParam String usuario,
            @Valid @RequestBody CommentEntity comment) {
        CommentEntity updatedComment = commentService.updateComment(id, usuario, comment.getContenido());
        return ResponseEntity.ok(updatedComment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @RequestParam String usuario) {
        commentService.deleteComment(id, usuario);
        return ResponseEntity.noContent().build();
    }
}