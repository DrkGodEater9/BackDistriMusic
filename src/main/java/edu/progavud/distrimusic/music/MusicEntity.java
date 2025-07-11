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

/**
 * Entidad que representa una canción en el sistema DistriMusic.
 * 
 * Esta clase maneja la persistencia y estructura de las canciones en la aplicación.
 * Cada canción tiene información básica como título, artista y álbum, así como
 * relaciones con las playlists que la contienen. Implementa equals y hashCode
 * basados únicamente en el ID para evitar problemas de referencias circulares.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Entity
@Table(name = "songs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicEntity {
    
    /**
     * Identificador único de la canción.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Título de la canción.
     * No puede ser nulo ni estar vacío.
     */
    @Column(nullable = false)
    @NotBlank(message = "El título es obligatorio")
    private String titulo;
    
    /**
     * Nombre del artista o banda.
     * No puede ser nulo ni estar vacío.
     */
    @Column(nullable = false)
    @NotBlank(message = "El artista es obligatorio")
    private String artista;
    
    /**
     * Nombre del álbum al que pertenece la canción.
     * No puede ser nulo ni estar vacío.
     */
    @Column(nullable = false)
    @NotBlank(message = "El álbum es obligatorio")
    private String album;
    
    /**
     * URL de la imagen del álbum o portada de la canción.
     * Este campo es opcional.
     */
    @Column(name = "image_url")
    private String imageUrl;
    
    /**
     * Fecha y hora de cuando la canción fue agregada al sistema.
     * Se genera automáticamente y no es modificable.
     */
    @CreationTimestamp
    @Column(name = "fecha_publicacion", nullable = false, updatable = false)
    private LocalDateTime fechaPublicacion;
    
    /**
     * Conjunto de playlists que contienen esta canción.
     * Se utiliza @JsonIgnore para evitar referencias circulares en la serialización.
     */
    @JsonIgnore
    @ManyToMany(mappedBy = "canciones", fetch = FetchType.LAZY)
    private Set<PlaylistEntity> playlists = new HashSet<>();
    
    /**
     * Constructor personalizado para crear una nueva canción.
     *
     * @param titulo título de la canción
     * @param artista nombre del artista
     * @param album nombre del álbum
     * @param imageUrl URL de la imagen (opcional)
     */
    public MusicEntity(String titulo, String artista, String album, String imageUrl) {
        this.titulo = titulo;
        this.artista = artista;
        this.album = album;
        this.imageUrl = imageUrl;
    }
    
    /**
     * Implementación de equals basada únicamente en el ID.
     * Esto evita problemas de referencias circulares y comparaciones profundas.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicEntity that = (MusicEntity) o;
        return Objects.equals(id, that.id);
    }
    
    /**
     * Implementación de hashCode basada únicamente en el ID.
     * Consistente con la implementación de equals.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Representación en string de la canción.
     * Incluye los campos principales sin relaciones para evitar referencias circulares.
     */
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