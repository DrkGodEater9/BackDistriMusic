package edu.progavud.distrimusic.playlist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    
    private final PlaylistRepository playlistRepository;
    
    public PlaylistEntity createPlaylist(PlaylistEntity playlist) {
        return playlistRepository.save(playlist);
    }
    
    public PlaylistEntity getPlaylistById(Long id) {
        return playlistRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));
    }
    
    public List<PlaylistEntity> getAllPlaylists() {
        return playlistRepository.findAll();
    }
    
    public List<PlaylistEntity> getPublicPlaylists() {
        return playlistRepository.findByEsPublicaTrue();
    }
    
    public List<PlaylistEntity> getPlaylistsByUser(String usuario) {
        return playlistRepository.findByUsuarioUsuario(usuario);
    }
    
    public PlaylistEntity updatePlaylist(Long id, PlaylistEntity playlistRequest) {
        PlaylistEntity existingPlaylist = getPlaylistById(id);
        
        if (playlistRequest.getNombre() != null) {
            existingPlaylist.setNombre(playlistRequest.getNombre());
        }
        if (playlistRequest.getEsPublica() != null) {
            existingPlaylist.setEsPublica(playlistRequest.getEsPublica());
        }
        
        return playlistRepository.save(existingPlaylist);
    }
    
    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }
}