package edu.progavud.distrimusic.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para gestionar la persistencia de comentarios.
 * 
 * Esta interfaz proporciona métodos para realizar operaciones CRUD sobre la entidad CommentEntity,
 * así como consultas personalizadas para recuperar comentarios basados en diferentes criterios.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    
    /**
     * Busca todos los comentarios de una playlist ordenados por ID de forma descendente.
     *
     * @param playlistId ID de la playlist
     * @return Lista de comentarios ordenados del más reciente al más antiguo
     */
    List<CommentEntity> findByPlaylistIdOrderByIdDesc(Long playlistId);
    
    /**
     * Busca todos los comentarios realizados por un usuario ordenados por ID de forma descendente.
     *
     * @param usuario Nombre del usuario
     * @return Lista de comentarios del usuario ordenados del más reciente al más antiguo
     */
    List<CommentEntity> findByUsuarioUsuarioOrderByIdDesc(String usuario);
    
    /**
     * Busca todos los comentarios de una playlist sin ordenar.
     *
     * @param playlistId ID de la playlist
     * @return Lista de comentarios sin orden específico
     */
    List<CommentEntity> findByPlaylistId(Long playlistId);
    
    /**
     * Elimina todos los comentarios asociados a una playlist.
     * Este método es útil cuando se elimina una playlist y se necesita eliminar sus comentarios.
     *
     * @param playlistId ID de la playlist cuyos comentarios serán eliminados
     */
    void deleteByPlaylistId(Long playlistId);
}