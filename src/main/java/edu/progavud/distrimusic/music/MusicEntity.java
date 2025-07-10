package edu.progavud.distrimusic.music;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import edu.progavud.distrimusic.playlist.PlaylistEntity;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

@Entity
@Table(name = "songs")
@Getter
@Setter
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
    
    // ✅ FIX: Usar @JsonIgnore para evitar referencias circulares
    @JsonIgnore
    @ManyToMany(mappedBy = "canciones", fetch = FetchType.LAZY)
    private Set<PlaylistEntity> playlists = new HashSet<>();
    
    // Constructor personalizado sin relaciones
    public MusicEntity(String titulo, String artista, String album, String imageUrl) {
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.imageUrl = imageUrl;
    }
    
    // ✅ FIX: Implementar hashCode y equals SOLO basado en ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicEntity that = (MusicEntity) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "MusicEntity{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", artista='" + artista + '\'' +
                ", album='" + album + '\'' +
                '}';
    }
}