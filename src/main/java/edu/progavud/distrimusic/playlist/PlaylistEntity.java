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

@Entity
@Table(name = "playlists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PlaylistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre de la playlist es obligatorio")
    private String nombre;

    @Column(name = "es_publica", nullable = false)
    private Boolean esPublica = true;

    @Column(name = "image_url")
    private String imageUrl;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "playlists", "password", "email", "seguidores", "siguiendo"})
    private UserEntity usuario;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private Set<MusicEntity> canciones = new HashSet<>();

    // Constructor personalizado
    public PlaylistEntity(String nombre, Boolean esPublica, UserEntity usuario) {
        this.nombre = nombre;
        this.esPublica = esPublica;
        this.usuario = usuario;
    }
    
    // ✅ Métodos helper para obtener información sin cargar relaciones
    public int getCantidadCanciones() {
        return canciones != null ? canciones.size() : 0;
    }
    
    public String getNombreUsuario() {
        return usuario != null ? usuario.getUsuario() : null;
    }
    
    // ✅ FIX: Implementar hashCode y equals SOLO basado en ID
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistEntity that = (PlaylistEntity) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
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