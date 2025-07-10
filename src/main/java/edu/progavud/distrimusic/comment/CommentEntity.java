package edu.progavud.distrimusic.comment;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import edu.progavud.distrimusic.persona.UserEntity;
import edu.progavud.distrimusic.playlist.PlaylistEntity;
import org.hibernate.annotations.CreationTimestamp;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 500)
    @NotBlank(message = "El contenido del comentario es obligatorio")
    @Size(max = 500, message = "El comentario no puede exceder 500 caracteres")
    private String contenido;
    
    // ✅ AGREGAR: Fecha del comentario
    @CreationTimestamp
    @Column(name = "fecha_comentario", nullable = false, updatable = false)
    private LocalDateTime fechaComentario;
    
    // Relación muchos a uno con usuario (muchos comentarios pueden pertenecer a un usuario)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comments", "playlists", "following", "followers"})
    private UserEntity usuario;
    
    // Relación muchos a uno con playlist (muchos comentarios pueden pertenecer a una playlist)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comments", "songs", "usuario"})
    private PlaylistEntity playlist;
    
    // Constructor personalizado
    public CommentEntity(String contenido, UserEntity usuario, PlaylistEntity playlist) {
        this.contenido = contenido;
        this.usuario = usuario;
        this.playlist = playlist;
    }
}