package edu.progavud.distrimusic.music;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import edu.progavud.distrimusic.playlist.PlaylistEntity;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "songs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    
    @Column(nullable = false)
    @NotBlank(message = "El artista es obligatorio")
    private String artista;
    
    @Column(nullable = false)
    @NotBlank(message = "El álbum es obligatorio")
    private String album;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    @CreationTimestamp
    @Column(name = "fecha_publicacion", nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;
    
    // Relación muchos a muchos con playlists (una canción puede estar en muchas playlists)
    @ManyToMany(mappedBy = "canciones", fetch = FetchType.LAZY)
    private Set<PlaylistEntity> playlists = new HashSet<>();
    
    // Constructor personalizado sin relaciones
    public MusicEntity(String titulo, String artista, String album, String imageUrl) {
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.imageUrl = imageUrl;
    }
}