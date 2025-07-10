package edu.progavud.distrimusic.playlist;

import lombok.RequiredArgsConstructor;
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
public class PlaylistController {
    
    private final PlaylistService playlistService;
    
    @PostMapping
    public ResponseEntity<PlaylistEntity> createPlaylist(@Valid @RequestBody PlaylistEntity playlist) {
        PlaylistEntity savedPlaylist = playlistService.createPlaylist(playlist);
        return new ResponseEntity<>(savedPlaylist, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PlaylistEntity> getPlaylistById(@PathVariable Long id) {
        PlaylistEntity playlist = playlistService.getPlaylistById(id);
        return ResponseEntity.ok(playlist);
    }
    
    @GetMapping
    public ResponseEntity<List<PlaylistEntity>> getAllPlaylists() {
        List<PlaylistEntity> playlists = playlistService.getAllPlaylists();
        return ResponseEntity.ok(playlists);
    }
    
    @GetMapping("/public")
    public ResponseEntity<List<PlaylistEntity>> getPublicPlaylists() {
        List<PlaylistEntity> playlists = playlistService.getPublicPlaylists();
        return ResponseEntity.ok(playlists);
    }
    
    @GetMapping("/user/{usuario}")
    public ResponseEntity<List<PlaylistEntity>> getPlaylistsByUser(@PathVariable String usuario) {
        List<PlaylistEntity> playlists = playlistService.getPlaylistsByUser(usuario);
        return ResponseEntity.ok(playlists);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistEntity> updatePlaylist(@PathVariable Long id, @Valid @RequestBody PlaylistEntity playlist) {
        PlaylistEntity updatedPlaylist = playlistService.updatePlaylist(id, playlist);
        return ResponseEntity.ok(updatedPlaylist);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable Long id) {
        playlistService.deletePlaylist(id);
        return ResponseEntity.noContent().build();
    }
    
    // Nuevos endpoints para manejar canciones en playlists
    
    @PostMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Map<String, String>> addSongToPlaylist(
            @PathVariable Long playlistId, 
            @PathVariable Long songId) {
        try {
            playlistService.addSongToPlaylist(playlistId, songId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Canción agregada a la playlist exitosamente");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Map<String, String>> removeSongFromPlaylist(
            @PathVariable Long playlistId, 
            @PathVariable Long songId) {
        try {
            playlistService.removeSongFromPlaylist(playlistId, songId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Canción eliminada de la playlist exitosamente");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            response.put("status", "error");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @GetMapping("/{playlistId}/songs")
    public ResponseEntity<Set<MusicEntity>> getPlaylistSongs(@PathVariable Long playlistId) {
        Set<MusicEntity> songs = playlistService.getPlaylistSongs(playlistId);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/{playlistId}/songs/{songId}/exists")
    public ResponseEntity<Map<String, Boolean>> isSongInPlaylist(
            @PathVariable Long playlistId, 
            @PathVariable Long songId) {
        boolean exists = playlistService.isSongInPlaylist(playlistId, songId);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}