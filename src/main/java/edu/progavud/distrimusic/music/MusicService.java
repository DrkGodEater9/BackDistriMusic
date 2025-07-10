package edu.progavud.distrimusic.music;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MusicService {
    
    private final MusicRepository musicRepository;
    
    public MusicEntity createSong(MusicEntity song) {
        return musicRepository.save(song);
    }
    
    public MusicEntity getSongById(Long id) {
        return musicRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Canción no encontrada"));
    }
    
    public List<MusicEntity> getAllSongs() {
        return musicRepository.findAll();
    }
    
    public List<MusicEntity> searchSongs(String query) {
        return musicRepository.findByTituloContainingIgnoreCaseOrArtistaContainingIgnoreCase(query, query);
    }
    
    public List<MusicEntity> getSongsByArtist(String artista) {
        return musicRepository.findByArtistaContainingIgnoreCase(artista);
    }
    
    public List<MusicEntity> getSongsByAlbum(String album) {
        return musicRepository.findByAlbumContainingIgnoreCase(album);
    }
    
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
    
    public void deleteSong(Long id) {
        musicRepository.deleteById(id);
    }
}