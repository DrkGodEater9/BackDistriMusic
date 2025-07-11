package edu.progavud.distrimusic.playlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para gestionar la persistencia de playlists.
 * 
 * Esta interfaz proporciona métodos para realizar operaciones CRUD sobre la entidad PlaylistEntity,
 * así como consultas personalizadas optimizadas para evitar carga innecesaria de datos.
 * Incluye métodos específicos para gestionar la visibilidad pública y privada de las playlists.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Repository
public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    
    /**
     * Obtiene todas las playlists públicas.
     *
     * @return lista de playlists públicas
     */
    List<PlaylistEntity> findByEsPublicaTrue();
    
    /**
     * Obtiene todas las playlists de un usuario específico.
     *
     * @param usuario nombre de usuario del propietario
     * @return lista de playlists del usuario
     */
    List<PlaylistEntity> findByUsuarioUsuario(String usuario);
    
    /**
     * Verifica si una canción está en una playlist específica.
     * Optimizado para no cargar la colección completa de canciones.
     *
     * @param playlistId ID de la playlist
     * @param songId ID de la canción
     * @return true si la canción está en la playlist
     */
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM PlaylistEntity p JOIN p.canciones c " +
           "WHERE p.id = :playlistId AND c.id = :songId")
    boolean existsByIdAndCancionesId(@Param("playlistId") Long playlistId, @Param("songId") Long songId);
    
    /**
     * Cuenta la cantidad de canciones en una playlist.
     * Optimizado para no cargar la colección completa de canciones.
     *
     * @param playlistId ID de la playlist
     * @return número de canciones en la playlist
     */
    @Query("SELECT COUNT(c) FROM PlaylistEntity p JOIN p.canciones c WHERE p.id = :playlistId")
    long countCancionesByPlaylistId(@Param("playlistId") Long playlistId);
}