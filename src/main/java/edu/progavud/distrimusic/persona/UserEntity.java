package edu.progavud.distrimusic.persona;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import edu.progavud.distrimusic.playlist.PlaylistEntity;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "usuarios")
@DiscriminatorValue("USUARIO")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends PersonaEntity {
    
    @Column(name = "codigo_estudiantil", unique = true, nullable = false)
    @NotBlank(message = "El código estudiantil es obligatorio")
    private String codigoEstudiantil;
    
    @Column(name = "carrera", nullable = false)
    @NotBlank(message = "La carrera es obligatoria")
    private String carrera;
    
    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;
    
    @Column(name = "ubicacion", nullable = false)
    @NotBlank(message = "La ubicación es obligatoria")
    private String ubicacion = "Bogotá D.C.";
    
    // ✅ CAMPO DE EMAIL PARA ENVÍO DE CORREO
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "El email debe tener un formato válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;
    
    // Relación uno a muchos con playlists
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlaylistEntity> playlists = new HashSet<>();
    
    // Constructor personalizado
    public UserEntity(String usuario, String nombre, String contraseña, String codigoEstudiantil, 
                     String carrera, String ubicacion, String email) {
        super(usuario, nombre, contraseña);
        this.codigoEstudiantil = codigoEstudiantil;
        this.carrera = carrera;
        this.ubicacion = ubicacion;
        this.email = email;
    }
    
    // Constructor sin ubicación (usa default)
    public UserEntity(String usuario, String nombre, String contraseña, String codigoEstudiantil, 
                     String carrera, String email) {
        this(usuario, nombre, contraseña, codigoEstudiantil, carrera, "Bogotá D.C.", email);
    }
    
    // Implementación del método abstracto (LSP - cumple el contrato)
    @Override
    public String getTipoPersona() {
        return "USUARIO";
    }
    
    // Sobrescribir método para comportamiento específico de Usuario (LSP)
    @Override
    public boolean puedeRealizarAccion(String accion) {
        switch (accion.toLowerCase()) {
            case "crear_playlist":
            case "comentar_playlist":
            case "dar_like":
                return true;
            case "eliminar_cualquier_playlist":
            case "administrar_usuarios":
                return false; // Solo administradores pueden hacer esto
            default:
                return super.puedeRealizarAccion(accion);
        }
    }
    
    // Método específico de Usuario
    public String getInfoEstudiantil() {
        return "Estudiante de " + carrera + " (Código: " + codigoEstudiantil + ")";
    }
    
    // Método para información completa
    public String getInfoCompleta() {
        return getDisplayName() + " - " + getInfoEstudiantil() + " - " + ubicacion;
    }
}