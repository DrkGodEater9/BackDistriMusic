package edu.progavud.distrimusic.playlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    
    List<PlaylistEntity> findByEsPublicaTrue();
    
    List<PlaylistEntity> findByUsuarioUsuario(String usuario);
    
    // ✅ FIX: Método para verificar si una canción está en una playlist SIN cargar la colección completa
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END " +
           "FROM PlaylistEntity p JOIN p.canciones c " +
           "WHERE p.id = :playlistId AND c.id = :songId")
    boolean existsByIdAndCancionesId(@Param("playlistId") Long playlistId, @Param("songId") Long songId);
    
    // ✅ Método adicional para obtener el conteo de canciones sin cargar la colección
    @Query("SELECT COUNT(c) FROM PlaylistEntity p JOIN p.canciones c WHERE p.id = :playlistId")
    long countCancionesByPlaylistId(@Param("playlistId") Long playlistId);
}