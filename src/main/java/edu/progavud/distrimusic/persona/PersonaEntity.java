package edu.progavud.distrimusic.persona;

/**
 * Clase abstracta base para la jerarquía de personas en el sistema DistriMusic.
 * 
 * Esta clase implementa el patrón de herencia con tabla por clase (JOINED) en JPA,
 * siguiendo el Principio de Sustitución de Liskov (LSP). Define la estructura básica
 * y comportamiento común para todos los tipos de usuarios en el sistema.
 * 
 * La contraseña debe cumplir con políticas de seguridad específicas:
 * - Mínimo 8 caracteres
 * - Al menos una letra mayúscula
 * - Al menos una letra minúscula
 * - Al menos un número
 * - Al menos un carácter especial (@$!%*?&)
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
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
    
    /**
     * Nombre de usuario único que identifica a la persona.
     * Sirve como clave primaria en la base de datos.
     */
    @Id
    @Column(name = "usuario", unique = true, nullable = false)
    @NotBlank(message = "El usuario es obligatorio")
    protected String usuario;
    
    /**
     * Nombre completo de la persona.
     */
    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    protected String nombre;
    
    /**
     * Contraseña de la cuenta.
     * Debe cumplir con requisitos específicos de seguridad definidos en la expresión regular.
     */
    @Column(name = "contraseña", nullable = false)
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial"
    )
    protected String contraseña;
    
    /**
     * Obtiene el tipo específico de persona.
     * Las clases hijas deben implementar este método para identificar su tipo.
     *
     * @return String que identifica el tipo de persona
     */
    public abstract String getTipoPersona();
    
    /**
     * Obtiene el nombre de visualización de la persona.
     * Formato: "nombre (@usuario)".
     *
     * @return String con el nombre formateado para mostrar
     */
    public String getDisplayName() {
        return nombre + " (@" + usuario + ")";
    }
    
    /**
     * Verifica si la persona puede realizar una acción específica.
     * Por defecto permite todas las acciones básicas.
     *
     * @param accion nombre de la acción a verificar
     * @return true si la persona puede realizar la acción
     */
    public boolean puedeRealizarAccion(String accion) {
        return true; // Por defecto, cualquier persona puede realizar acciones básicas
    }
    
    /**
     * Valida las credenciales de la persona.
     *
     * @param contraseñaIngresada contraseña a validar
     * @return true si la contraseña coincide con la almacenada
     */
    public boolean validarCredenciales(String contraseñaIngresada) {
        return this.contraseña.equals(contraseñaIngresada);
    }
}