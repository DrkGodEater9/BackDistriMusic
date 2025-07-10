package edu.progavud.distrimusic.persona;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "personas")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_persona", discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class PersonaEntity {
    
    @Id
    @Column(name = "usuario", unique = true, nullable = false)
    @NotBlank(message = "El usuario es obligatorio")
    protected String usuario;
    
    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    protected String nombre;
    
    @Column(name = "contraseña", nullable = false)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial"
    )
    protected String contraseña;
    
    // Método abstracto que deben implementar las clases hijas (LSP)
    public abstract String getTipoPersona();
    
    // Método concreto que pueden usar todas las clases hijas (LSP)
    public String getDisplayName() {
        return nombre + " (@" + usuario + ")";
    }
    
    // Método que puede ser sobrescrito (LSP)
    public boolean puedeRealizarAccion(String accion) {
        return true; // Por defecto, cualquier persona puede realizar acciones básicas
    }
    
    // Método para verificar credenciales (LSP)
    public boolean validarCredenciales(String contraseñaIngresada) {
        return this.contraseña.equals(contraseñaIngresada);
    }
}