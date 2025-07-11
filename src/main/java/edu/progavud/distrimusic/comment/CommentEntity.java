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

/**
 * Entidad que representa un comentario en una playlist.
 * 
 * Esta clase maneja la persistencia y estructura de los comentarios en la aplicación.
 * Los comentarios están asociados a un usuario autor y a una playlist específica.
 * Se utiliza la anotación {@code @JsonIgnoreProperties} para manejar la serialización
 * de las relaciones lazy loading con Hibernate.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentEntity {
    
    /**
     * Identificador único del comentario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Contenido del comentario.
     * No puede ser nulo ni estar vacío y tiene un límite de 500 caracteres.
     */
    @Column(nullable = false, length = 500)
    @NotBlank(message = "El contenido del comentario es obligatorio")
    @Size(max = 500, message = "El comentario no puede exceder 500 caracteres")
    private String contenido;
    
    /**
     * Fecha y hora de creación del comentario.
     * Se genera automáticamente al crear el comentario y no es modificable.
     */
    @CreationTimestamp
    @Column(name = "fecha_comentario", nullable = false, updatable = false)
    private LocalDateTime fechaComentario;
    
    /**
     * Usuario que creó el comentario.
     * Relación muchos a uno con la entidad UserEntity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comments", "playlists", "following", "followers"})
    private UserEntity usuario;
    
    /**
     * Playlist en la que se realizó el comentario.
     * Relación muchos a uno con la entidad PlaylistEntity.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "comments", "songs", "usuario"})
    private PlaylistEntity playlist;
    
    /**
     * Constructor personalizado para crear un nuevo comentario.
     * 
     * @param contenido Texto del comentario
     * @param usuario Usuario que crea el comentario
     * @param playlist Playlist donde se crea el comentario
     */
    public CommentEntity(String contenido, UserEntity usuario, PlaylistEntity playlist) {
        this.contenido = contenido;
        this.usuario = usuario;
        this.playlist = playlist;
    }
}