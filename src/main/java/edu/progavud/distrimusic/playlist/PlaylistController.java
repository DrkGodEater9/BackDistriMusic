package edu.progavud.distrimusic.playlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import edu.progavud.distrimusic.music.MusicEntity;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class PlaylistController {
    
    private final PlaylistService playlistService;
    
    @PostMapping
    public ResponseEntity<PlaylistEntity> createPlaylist(@Valid @RequestBody PlaylistEntity playlist) {
        try {
            PlaylistEntity savedPlaylist = playlistService.createPlaylist(playlist);
            return new ResponseEntity<>(savedPlaylist, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error al crear playlist: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistEntity> getPlaylistById(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                log.warn("ID de playlist inválido: {}", id);
                return ResponseEntity.badRequest().build();
            }
            
            PlaylistEntity playlist = playlistService.getPlaylistById(id);
            return ResponseEntity.ok(playlist);
        } catch (NumberFormatException e) {
            log.error("Error al parsear ID de playlist: {}", id, e);
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Playlist no encontrada con ID: {}", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error inesperado al obtener playlist: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<PlaylistEntity>> getAllPlaylists() {
        try {
            List<PlaylistEntity> playlists = playlistService.getAllPlaylists();
            return ResponseEntity.ok(playlists);
        } catch (Exception e) {
            log.error("Error al obtener todas las playlists: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/public")
    public ResponseEntity<List<PlaylistEntity>> getPublicPlaylists() {
        try {
            List<PlaylistEntity> playlists = playlistService.getPublicPlaylists();
            return ResponseEntity.ok(playlists);
        } catch (Exception e) {
            log.error("Error al obtener playlists públicas: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/user/{usuario}")
    public ResponseEntity<List<PlaylistEntity>> getPlaylistsByUser(@PathVariable String usuario) {
        try {
            List<PlaylistEntity> playlists = playlistService.getPlaylistsByUser(usuario);
            return ResponseEntity.ok(playlists);
        } catch (Exception e) {
            log.error("Error al obtener playlists del usuario {}: ", usuario, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistEntity> updatePlaylist(@PathVariable Long id, @Valid @RequestBody PlaylistEntity playlist) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            PlaylistEntity updatedPlaylist = playlistService.updatePlaylist(id, playlist);
            return ResponseEntity.ok(updatedPlaylist);
        } catch (RuntimeException e) {
            log.error("Error al actualizar playlist {}: ", id, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error inesperado al actualizar playlist: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        try {
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            playlistService.deletePlaylist(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error al eliminar playlist {}: ", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    // ✅ FIX: Usar String en lugar de Map para respuestas simples
    
    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<String> addSongToPlaylist(
            @PathVariable Long playlistId, 
            @PathVariable Long songId) {
        try {
            if (playlistId == null || playlistId <= 0 || songId == null || songId <= 0) {
                return ResponseEntity.badRequest().body("IDs de playlist y canción deben ser válidos");
            }
            
            playlistService.addSongToPlaylist(playlistId, songId);
            return ResponseEntity.ok("Canción agregada a la playlist exitosamente");
        } catch (RuntimeException e) {
            log.error("Error al agregar canción {} a playlist {}: ", songId, playlistId, e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al agregar canción: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
    
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<String> removeSongFromPlaylist(
            @PathVariable Long playlistId, 
            @PathVariable Long songId) {
        try {
            if (playlistId == null || playlistId <= 0 || songId == null || songId <= 0) {
                return ResponseEntity.badRequest().body("IDs de playlist y canción deben ser válidos");
            }
            
            playlistService.removeSongFromPlaylist(playlistId, songId);
            return ResponseEntity.ok("Canción eliminada de la playlist exitosamente");
        } catch (RuntimeException e) {
            log.error("Error al eliminar canción {} de playlist {}: ", songId, playlistId, e);
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error inesperado al eliminar canción: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }
    
    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<Set<MusicEntity>> getPlaylistSongs(@PathVariable Long playlistId) {
        try {
            if (playlistId == null || playlistId <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            Set<MusicEntity> songs = playlistService.getPlaylistSongs(playlistId);
            return ResponseEntity.ok(songs);
        } catch (RuntimeException e) {
            log.error("Error al obtener canciones de playlist {}: ", playlistId, e);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Error inesperado al obtener canciones: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping("/{playlistId}/songs/{songId}/exists")
    public ResponseEntity<Boolean> isSongInPlaylist(
            @PathVariable Long playlistId, 
            @PathVariable Long songId) {
        try {
            if (playlistId == null || playlistId <= 0 || songId == null || songId <= 0) {
                return ResponseEntity.badRequest().build();
            }
            
            boolean exists = playlistService.isSongInPlaylist(playlistId, songId);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            log.error("Error al verificar si canción está en playlist: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}