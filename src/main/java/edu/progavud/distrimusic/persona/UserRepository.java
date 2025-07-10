package edu.progavud.distrimusic.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    
    // Método que necesita UserService
    boolean existsByEmail(String email);
    
    // LSP: Buscar por nombre (hereda de PersonaEntity)
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<UserEntity> findByNombreContainingIgnoreCase(String nombre);
    
    // LSP: Método que trabaja con campos de PersonaEntity
    @Query("SELECT u FROM UserEntity u WHERE u.usuario = :usuario AND u.contraseña = :contraseña")
    Optional<UserEntity> findByUsuarioAndContraseña(String usuario, String contraseña);
    
    // ✅ NUEVOS: Métodos para sistema de seguimiento
    
    // Obtener seguidores de un usuario
    @Query("SELECT u.seguidores FROM UserEntity u WHERE u.usuario = :usuario")
    List<UserEntity> findSeguidoresByUsuario(String usuario);
    
    // Obtener usuarios que sigue un usuario
    @Query("SELECT u.siguiendo FROM UserEntity u WHERE u.usuario = :usuario")
    List<UserEntity> findSiguiendoByUsuario(String usuario);
    
    // Verificar si un usuario sigue a otro
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
           "FROM UserEntity u JOIN u.siguiendo s WHERE u.usuario = :follower AND s.usuario = :following")
    boolean isFollowing(String follower, String following);
    
    
}