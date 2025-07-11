package edu.progavud.distrimusic.playlist;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import edu.progavud.distrimusic.persona.UserEntity;
import edu.progavud.distrimusic.music.MusicEntity;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import java.util.Objects;

/**
 * Entidad que representa una playlist en el sistema DistriMusic.
 * 
 * Esta clase maneja la persistencia y estructura de las playlists, incluyendo
 * sus relaciones con usuarios y canciones. Implementa la gestión de lazy loading
 * con Hibernate y evita referencias circulares en la serialización JSON.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Entity
@Table(name = "playlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlaylistEntity {

    /**
     * Identificador único de la playlist.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la playlist.
     * No puede ser nulo ni estar vacío.
     */
    @Column(nullable = false)
    @NotBlank(message = "El nombre de la playlist es obligatorio")
    private String nombre;

    /**
     * Indica si la playlist es pública o privada.
     * Por defecto, las playlists son públicas.
     */
    @Column(name = "es_publica", nullable = false)
    private Boolean esPublica = true;

    /**
     * URL de la imagen de portada de la playlist.
     * Es opcional.
     */
    @Column(name = "image_url")
    private String imageUrl;

    /**
     * Fecha y hora de creación de la playlist.
     * Se genera automáticamente y no es modificable.
     */
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    /**
     * Usuario propietario de la playlist.
     * Se usa @JsonIgnoreProperties para evitar referencias circulares y datos sensibles.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "playlists", "password", "email", "seguidores", "siguiendo"})
    private UserEntity usuario;

    /**
     * Conjunto de canciones en la playlist.
     * Utiliza una tabla de unión para la relación many-to-many.
     */
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private Set<MusicEntity> canciones = new HashSet<>();

    /**
     * Constructor personalizado para crear una nueva playlist.
     *
     * @param nombre nombre de la playlist
     * @param esPublica indica si la playlist es pública
     * @param usuario propietario de la playlist
     */
    public PlaylistEntity(String nombre, Boolean esPublica, UserEntity usuario) {
        this.nombre = nombre;
        this.esPublica = esPublica;
        this.usuario = usuario;
    }
    
    /**
     * Obtiene la cantidad de canciones en la playlist.
     * Método helper que evita cargar la relación completa.
     *
     * @return número de canciones en la playlist
     */
    public int getCantidadCanciones() {
        return canciones != null ? canciones.size() : 0;
    }
    
    /**
     * Obtiene el nombre de usuario del propietario.
     * Método helper que evita cargar la relación completa.
     *
     * @return nombre de usuario del propietario
     */
    public String getNombreUsuario() {
        return usuario != null ? usuario.getUsuario() : null;
    }
    
    /**
     * Implementación de equals basada únicamente en el ID.
     * Evita problemas con lazy loading y referencias circulares.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistEntity that = (PlaylistEntity) o;
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
     * Representación en string de la playlist.
     * Incluye información básica y evita cargar relaciones completas.
     */
    @Override
    public String toString() {
        return "PlaylistEntity{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", esPublica=" + esPublica +
                ", usuario=" + (usuario != null ? usuario.getUsuario() : null) +
                '}';
    }
}