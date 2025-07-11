package edu.progavud.distrimusic.music;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositorio para gestionar la persistencia de canciones.
 * 
 * Esta interfaz proporciona métodos para realizar operaciones CRUD sobre la entidad MusicEntity,
 * así como consultas personalizadas para buscar canciones por diferentes criterios.
 * Todas las búsquedas son case-insensitive para mejorar la experiencia de usuario.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Repository
public interface MusicRepository extends JpaRepository<MusicEntity, Long> {
    
    /**
     * Busca canciones que coincidan parcialmente con el título o artista especificados.
     * La búsqueda es case-insensitive.
     *
     * @param titulo término de búsqueda para el título
     * @param artista término de búsqueda para el artista
     * @return lista de canciones que coinciden con alguno de los criterios
     */
    List<MusicEntity> findByTituloContainingIgnoreCaseOrArtistaContainingIgnoreCase(String titulo, String artista);
    
    /**
     * Busca canciones de un artista específico.
     * La búsqueda es case-insensitive y parcial.
     *
     * @param artista nombre o parte del nombre del artista
     * @return lista de canciones del artista
     */
    List<MusicEntity> findByArtistaContainingIgnoreCase(String artista);
    
    /**
     * Busca canciones de un álbum específico.
     * La búsqueda es case-insensitive y parcial.
     *
     * @param album nombre o parte del nombre del álbum
     * @return lista de canciones del álbum
     */
    List<MusicEntity> findByAlbumContainingIgnoreCase(String album);
    
    /**
     * Obtiene todas las canciones ordenadas alfabéticamente por título.
     *
     * @return lista de todas las canciones ordenadas por título
     */
    List<MusicEntity> findAllByOrderByTituloAsc();
}