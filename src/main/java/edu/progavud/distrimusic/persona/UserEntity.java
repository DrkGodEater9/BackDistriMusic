package edu.progavud.distrimusic.persona;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * Entidad que representa un usuario estudiante en el sistema DistriMusic.
 * 
 * Esta clase maneja los usuarios del sistema, incluyendo sus datos personales,
 * relaciones sociales (seguidores/siguiendo) y playlists. Implementa la gestión
 * de lazy loading con Hibernate y evita referencias circulares en la serialización.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity {
    
    /**
     * URL de la imagen de perfil del usuario.
     */
    @Column(name = "profile_image_url")
    private String profileImageUrl;

    /**
     * Identificador único del usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de usuario único en el sistema.
     */
    @Column(unique = true, nullable = false)
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    /**
     * Contraseña del usuario.
     */
    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    /**
     * Correo electrónico único del usuario.
     */
    @Column(unique = true, nullable = false)
    @Email(message = "El email debe ser válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    /**
     * Nombre completo del usuario.
     */
    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    /**
     * Carrera universitaria del estudiante.
     */
    @Column(name = "carrera")
    private String carrera;

    /**
     * Código de identificación estudiantil.
     */
    @Column(name = "codigo_estudiantil")
    private String codigoEstudiantil;

    /**
     * Fecha y hora de registro del usuario.
     * Se genera automáticamente y no es modificable.
     */
    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    /**
     * Playlists creadas por el usuario.
     * Se usa @JsonIgnore para evitar referencias circulares.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PlaylistEntity> playlists = new HashSet<>();

    /**
     * Usuarios que siguen a este usuario.
     * Se usa @JsonIgnore para evitar referencias circulares.
     */
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_followers",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "follower_id")
    )
    private Set<UserEntity> seguidores = new HashSet<>();

    /**
     * Usuarios que este usuario sigue.
     * Se usa @JsonIgnore para evitar referencias circulares.
     */
    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_following",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<UserEntity> siguiendo = new HashSet<>();

    /**
     * Valida las credenciales del usuario.
     *
     * @param contraseñaIngresada contraseña a validar
     * @return true si la contraseña coincide
     */
    public boolean validarCredenciales(String contraseñaIngresada) {
        return this.password.equals(contraseñaIngresada);
    }

    /**
     * Verifica si este usuario sigue a otro usuario.
     *
     * @param usuario usuario a verificar
     * @return true si este usuario sigue al usuario especificado
     */
    public boolean estaSiguiendo(UserEntity usuario) {
        return this.siguiendo.contains(usuario);
    }

    /**
     * Obtiene la contraseña del usuario.
     * Método getter para compatibilidad con PersonaEntity.
     *
     * @return la contraseña del usuario
     */
    public String getContraseña() {
        return this.password;
    }

    /**
     * Implementación de equals basada únicamente en el ID.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id);
    }
    
    /**
     * Implementación de hashCode basada únicamente en el ID.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    /**
     * Representación en string del usuario.
     * Incluye los campos principales sin relaciones.
     */
    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", usuario='" + usuario + '\'' +
                ", email='" + email + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}