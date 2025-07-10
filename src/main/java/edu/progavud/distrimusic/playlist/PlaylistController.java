package edu.progavud.distrimusic.playlist;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

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
}