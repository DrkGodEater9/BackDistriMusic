package edu.progavud.distrimusic.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    // ✅ Buscar por usuario (String) - PRINCIPAL
    Optional<UserEntity> findByUsuario(String usuario);
    boolean existsByUsuario(String usuario);
    
    // ✅ Método que necesita UserService
    boolean existsByEmail(String email);
    
    // ✅ Buscar por nombre
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<UserEntity> findByNombreContainingIgnoreCase(String nombre);
    
    // ✅ Método para autenticación
    @Query("SELECT u FROM UserEntity u WHERE u.usuario = :usuario AND u.password = :password")
    Optional<UserEntity> findByUsuarioAndPassword(String usuario, String password);
    
    // ✅ Métodos para sistema de seguimiento
    @Query("SELECT u.seguidores FROM UserEntity u WHERE u.usuario = :usuario")
    List<UserEntity> findSeguidoresByUsuario(String usuario);
    
    @Query("SELECT u.siguiendo FROM UserEntity u WHERE u.usuario = :usuario")
    List<UserEntity> findSiguiendoByUsuario(String usuario);
    
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
           "FROM UserEntity u JOIN u.siguiendo s WHERE u.usuario = :follower AND s.usuario = :following")
    boolean isFollowing(String follower, String following);
}