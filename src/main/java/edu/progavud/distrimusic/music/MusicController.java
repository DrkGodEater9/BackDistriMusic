package edu.progavud.distrimusic.music;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Controlador REST para la gestión de canciones.
 * 
 * Esta clase expone los endpoints REST para realizar operaciones CRUD sobre las canciones
 * en el sistema DistriMusic. Proporciona endpoints para crear, buscar, actualizar y 
 * eliminar canciones, así como realizar búsquedas por diferentes criterios.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MusicController {
    
    private final MusicService musicService;
    
    /**
     * Crea una nueva canción en el sistema.
     *
     * @param song datos de la canción a crear
     * @return ResponseEntity con la canción creada y status 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<MusicEntity> createSong(@Valid @RequestBody MusicEntity song) {
        MusicEntity savedSong = musicService.createSong(song);
        return new ResponseEntity<>(savedSong, HttpStatus.CREATED);
    }
    
    /**
     * Obtiene una canción específica por su ID.
     *
     * @param id identificador de la canción
     * @return ResponseEntity con la canción encontrada
     * @throws RuntimeException si la canción no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<MusicEntity> getSongById(@PathVariable Long id) {
        MusicEntity song = musicService.getSongById(id);
        return ResponseEntity.ok(song);
    }
    
    /**
     * Obtiene todas las canciones del sistema.
     *
     * @return ResponseEntity con la lista de todas las canciones
     */
    @GetMapping
    public ResponseEntity<List<MusicEntity>> getAllSongs() {
        List<MusicEntity> songs = musicService.getAllSongs();
        return ResponseEntity.ok(songs);
    }
    
    /**
     * Busca canciones por título o artista.
     *
     * @param query término de búsqueda
     * @return ResponseEntity con la lista de canciones que coinciden con la búsqueda
     */
    @GetMapping("/search")
    public ResponseEntity<List<MusicEntity>> searchSongs(@RequestParam String query) {
        List<MusicEntity> songs = musicService.searchSongs(query);
        return ResponseEntity.ok(songs);
    }
    
    /**
     * Obtiene todas las canciones de un artista específico.
     *
     * @param artista nombre del artista
     * @return ResponseEntity con la lista de canciones del artista
     */
    @GetMapping("/artist/{artista}")
    public ResponseEntity<List<MusicEntity>> getSongsByArtist(@PathVariable String artista) {
        List<MusicEntity> songs = musicService.getSongsByArtist(artista);
        return ResponseEntity.ok(songs);
    }
    
    /**
     * Obtiene todas las canciones de un álbum específico.
     *
     * @param album nombre del álbum
     * @return ResponseEntity con la lista de canciones del álbum
     */
    @GetMapping("/album/{album}")
    public ResponseEntity<List<MusicEntity>> getSongsByAlbum(@PathVariable String album) {
        List<MusicEntity> songs = musicService.getSongsByAlbum(album);
        return ResponseEntity.ok(songs);
    }
    
    /**
     * Actualiza los datos de una canción existente.
     *
     * @param id identificador de la canción a actualizar
     * @param song nuevos datos de la canción
     * @return ResponseEntity con la canción actualizada
     * @throws RuntimeException si la canción no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<MusicEntity> updateSong(@PathVariable Long id, @Valid @RequestBody MusicEntity song) {
        MusicEntity updatedSong = musicService.updateSong(id, song);
        return ResponseEntity.ok(updatedSong);
    }
    
    /**
     * Elimina una canción del sistema.
     *
     * @param id identificador de la canción a eliminar
     * @return ResponseEntity sin contenido y status 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        musicService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}