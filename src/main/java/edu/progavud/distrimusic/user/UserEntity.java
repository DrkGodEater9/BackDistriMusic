
package edu.progavud.distrimusic.user;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    
    @Id
    @Column(name = "usuario", unique = true, nullable = false)
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario; // "Tu nickname único"
    
    @Column(name="nombre", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre; // "Tu nombre completo"
    
    @Column(name="contraseña", nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    private String contraseña; // "Mínimo 8 caracteres" - validación en frontend
    
    @Column(name = "codigo_estudiantil", unique = true, nullable = false)
    @NotBlank(message = "El código estudiantil es obligatorio")
    private String codigoEstudiantil; // "Tu código de estudiante"
    
    @Column(name="carrera", nullable = false)
    @NotBlank(message = "La carrera es obligatoria")
    private String carrera; // "Selecciona tu carrera" - dropdown
}
