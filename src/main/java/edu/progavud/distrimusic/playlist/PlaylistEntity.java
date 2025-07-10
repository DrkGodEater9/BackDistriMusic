package edu.progavud.distrimusic.playlist;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import edu.progavud.distrimusic.persona.UserEntity;
import edu.progavud.distrimusic.music.MusicEntity;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "playlists")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @NotBlank(message = "El nombre de la playlist es obligatorio")
    private String nombre;
    
    @Column(nullable = false)
    private Boolean esPublica = false;
    
    @Column(nullable = false)
    private Integer likes = 0;
    
    // ✅ NUEVO: URL de imagen de la playlist
    @Column(name = "image_url")
    private String imageUrl;
    
    // ✅ AGREGAR: Fecha de creación
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    // Relación muchos a uno con usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UserEntity usuario;
    
    // Relación muchos a muchos con canciones
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
}