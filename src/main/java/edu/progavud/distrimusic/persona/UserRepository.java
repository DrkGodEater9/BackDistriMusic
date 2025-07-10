package edu.progavud.distrimusic.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
        
    // LSP: Buscar por nombre (hereda de PersonaEntity)
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<UserEntity> findByNombreContainingIgnoreCase(String nombre);
    
    // LSP: Método que trabaja con campos de PersonaEntity
    @Query("SELECT u FROM UserEntity u WHERE u.usuario = :usuario AND u.contraseña = :contraseña")
    Optional<UserEntity> findByUsuarioAndContraseña(String usuario, String contraseña);
    
}