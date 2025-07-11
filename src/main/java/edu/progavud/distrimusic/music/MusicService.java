package edu.progavud.distrimusic.music;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Servicio que gestiona la lógica de negocio relacionada con las canciones.
 * 
 * Esta clase implementa las operaciones CRUD y la lógica de negocio para gestionar
 * las canciones en el sistema DistriMusic. Proporciona métodos para crear, buscar,
 * actualizar y eliminar canciones, así como realizar búsquedas por diferentes criterios.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Service
@RequiredArgsConstructor
public class MusicService {
    
    private final MusicRepository musicRepository;
    
    /**
     * Crea una nueva canción en el sistema.
     *
     * @param song entidad con los datos de la canción a crear
     * @return la canción creada con su ID asignado
     */
    public MusicEntity createSong(MusicEntity song) {
        return musicRepository.save(song);
    }
    
    /**
     * Obtiene una canción por su ID.
     *
     * @param id identificador de la canción
     * @return la canción encontrada
     * @throws RuntimeException si la canción no existe
     */
    public MusicEntity getSongById(Long id) {
        return musicRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Canción no encontrada"));
    }
    
    /**
     * Obtiene todas las canciones del sistema.
     *
     * @return lista de todas las canciones
     */
    public List<MusicEntity> getAllSongs() {
        return musicRepository.findAll();
    }
    
    /**
     * Busca canciones por título o artista.
     *
     * @param query término de búsqueda a encontrar en título o artista
     * @return lista de canciones que coinciden con la búsqueda
     */
    public List<MusicEntity> searchSongs(String query) {
        return musicRepository.findByTituloContainingIgnoreCaseOrArtistaContainingIgnoreCase(query, query);
    }
    
    /**
     * Obtiene todas las canciones de un artista específico.
     *
     * @param artista nombre del artista a buscar
     * @return lista de canciones del artista
     */
    public List<MusicEntity> getSongsByArtist(String artista) {
        return musicRepository.findByArtistaContainingIgnoreCase(artista);
    }
    
    /**
     * Obtiene todas las canciones de un álbum específico.
     *
     * @param album nombre del álbum a buscar
     * @return lista de canciones del álbum
     */
    public List<MusicEntity> getSongsByAlbum(String album) {
        return musicRepository.findByAlbumContainingIgnoreCase(album);
    }
    
    /**
     * Actualiza los datos de una canción existente.
     * Solo actualiza los campos que no son nulos en la solicitud.
     *
     * @param id identificador de la canción a actualizar
     * @param songRequest entidad con los nuevos datos de la canción
     * @return la canción actualizada
     * @throws RuntimeException si la canción no existe
     */
    public MusicEntity updateSong(Long id, MusicEntity songRequest) {
        MusicEntity existingSong = getSongById(id);
        
        if (songRequest.getTitulo() != null) {
            existingSong.setTitulo(songRequest.getTitulo());
        }
        if (songRequest.getArtista() != null) {
            existingSong.setArtista(songRequest.getArtista());
        }
        if (songRequest.getAlbum() != null) {
            existingSong.setAlbum(songRequest.getAlbum());
        }
        if (songRequest.getImageUrl() != null) {
            existingSong.setImageUrl(songRequest.getImageUrl());
        }
        
        return musicRepository.save(existingSong);
    }
    
    /**
     * Elimina una canción del sistema.
     *
     * @param id identificador de la canción a eliminar
     */
    public void deleteSong(Long id) {
        musicRepository.deleteById(id);
    }
}