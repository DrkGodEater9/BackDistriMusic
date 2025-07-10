package edu.progavud.distrimusic.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    
    // Buscar por código estudiantil
    Optional<UserEntity> findByCodigoEstudiantil(String codigoEstudiantil);
    
    // Verificar si existe código estudiantil
    boolean existsByCodigoEstudiantil(String codigoEstudiantil);
    
    // Buscar usuarios por carrera
    List<UserEntity> findByCarrera(String carrera);
    
    // LSP: Buscar por nombre (hereda de PersonaEntity)
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<UserEntity> findByNombreContainingIgnoreCase(String nombre);
    
    // LSP: Método que trabaja con campos de PersonaEntity
    @Query("SELECT u FROM UserEntity u WHERE u.usuario = :usuario AND u.contraseña = :contraseña")
    Optional<UserEntity> findByUsuarioAndContraseña(String usuario, String contraseña);
    
    // Buscar usuarios por ubicación
    List<UserEntity> findByUbicacionContainingIgnoreCase(String ubicacion);
    
    // Buscar usuarios registrados en un rango de fechas
    @Query("SELECT u FROM UserEntity u WHERE u.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<UserEntity> findByFechaRegistroBetween(java.time.LocalDateTime fechaInicio, java.time.LocalDateTime fechaFin);
}