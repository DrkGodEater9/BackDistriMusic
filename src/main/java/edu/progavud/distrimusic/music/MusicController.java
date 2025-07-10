package edu.progavud.distrimusic.music;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/music")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MusicController {
    
    private final MusicService musicService;
    
    @PostMapping
    public ResponseEntity<MusicEntity> createSong(@Valid @RequestBody MusicEntity song) {
        MusicEntity savedSong = musicService.createSong(song);
        return new ResponseEntity<>(savedSong, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MusicEntity> getSongById(@PathVariable Long id) {
        MusicEntity song = musicService.getSongById(id);
        return ResponseEntity.ok(song);
    }
    
    @GetMapping
    public ResponseEntity<List<MusicEntity>> getAllSongs() {
        List<MusicEntity> songs = musicService.getAllSongs();
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<MusicEntity>> searchSongs(@RequestParam String query) {
        List<MusicEntity> songs = musicService.searchSongs(query);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/artist/{artista}")
    public ResponseEntity<List<MusicEntity>> getSongsByArtist(@PathVariable String artista) {
        List<MusicEntity> songs = musicService.getSongsByArtist(artista);
        return ResponseEntity.ok(songs);
    }
    
    @GetMapping("/album/{album}")
    public ResponseEntity<List<MusicEntity>> getSongsByAlbum(@PathVariable String album) {
        List<MusicEntity> songs = musicService.getSongsByAlbum(album);
        return ResponseEntity.ok(songs);
    }
    
    @PostMapping("/{id}/like")
    public ResponseEntity<MusicEntity> likeSong(@PathVariable Long id) {
        MusicEntity song = musicService.likeSong(id);
        return ResponseEntity.ok(song);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MusicEntity> updateSong(@PathVariable Long id, @Valid @RequestBody MusicEntity song) {
        MusicEntity updatedSong = musicService.updateSong(id, song);
        return ResponseEntity.ok(updatedSong);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        musicService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}