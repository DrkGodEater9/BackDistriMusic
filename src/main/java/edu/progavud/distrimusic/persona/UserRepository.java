package edu.progavud.distrimusic.persona;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para gestionar la persistencia de usuarios.
 * 
 * Esta interfaz proporciona métodos para realizar operaciones CRUD sobre la entidad UserEntity,
 * así como consultas personalizadas para búsqueda, autenticación y gestión de relaciones
 * sociales entre usuarios.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    /**
     * Busca un usuario por su nombre de usuario.
     * Este es el método principal de búsqueda de usuarios.
     *
     * @param usuario nombre de usuario a buscar
     * @return Optional con el usuario si existe
     */
    Optional<UserEntity> findByUsuario(String usuario);
    
    /**
     * Verifica si existe un usuario con el nombre de usuario especificado.
     *
     * @param usuario nombre de usuario a verificar
     * @return true si el usuario existe
     */
    boolean existsByUsuario(String usuario);
    
    /**
     * Verifica si existe un usuario con el email especificado.
     * Utilizado durante el registro para evitar duplicados.
     *
     * @param email email a verificar
     * @return true si existe un usuario con ese email
     */
    boolean existsByEmail(String email);
    
    /**
     * Busca usuarios cuyo nombre contenga el texto especificado.
     * La búsqueda es case-insensitive.
     *
     * @param nombre texto a buscar en los nombres
     * @return lista de usuarios que coinciden con la búsqueda
     */
    @Query("SELECT u FROM UserEntity u WHERE LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<UserEntity> findByNombreContainingIgnoreCase(String nombre);
    
    /**
     * Autentica un usuario por sus credenciales.
     *
     * @param usuario nombre de usuario
     * @param password contraseña
     * @return Optional con el usuario si las credenciales son correctas
     */
    @Query("SELECT u FROM UserEntity u WHERE u.usuario = :usuario AND u.password = :password")
    Optional<UserEntity> findByUsuarioAndPassword(String usuario, String password);
    
    /**
     * Obtiene la lista de usuarios que siguen a un usuario específico.
     *
     * @param usuario nombre del usuario del cual obtener seguidores
     * @return lista de usuarios seguidores
     */
    @Query("SELECT u.seguidores FROM UserEntity u WHERE u.usuario = :usuario")
    List<UserEntity> findSeguidoresByUsuario(String usuario);
    
    /**
     * Obtiene la lista de usuarios que un usuario específico sigue.
     *
     * @param usuario nombre del usuario del cual obtener seguidos
     * @return lista de usuarios seguidos
     */
    @Query("SELECT u.siguiendo FROM UserEntity u WHERE u.usuario = :usuario")
    List<UserEntity> findSiguiendoByUsuario(String usuario);
    
    /**
     * Verifica si un usuario sigue a otro.
     *
     * @param follower nombre del usuario seguidor
     * @param following nombre del usuario seguido
     * @return true si follower sigue a following
     */
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
           "FROM UserEntity u JOIN u.siguiendo s WHERE u.usuario = :follower AND s.usuario = :following")
    boolean isFollowing(String follower, String following);
}