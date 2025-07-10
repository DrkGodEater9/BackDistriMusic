package edu.progavud.distrimusic.playlist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.progavud.distrimusic.music.MusicEntity;
import edu.progavud.distrimusic.music.MusicRepository;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PlaylistService {
    
    private final PlaylistRepository playlistRepository;
    private final MusicRepository musicRepository;
    
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
        if (playlistRequest.getImageUrl() != null) {
            existingPlaylist.setImageUrl(playlistRequest.getImageUrl());
        }
        
        return playlistRepository.save(existingPlaylist);
    }
    
    public void deletePlaylist(Long id) {
        playlistRepository.deleteById(id);
    }
    
    // Nuevos métodos para manejar canciones en playlists
    
    public PlaylistEntity addSongToPlaylist(Long playlistId, Long songId) {
        PlaylistEntity playlist = getPlaylistById(playlistId);
        MusicEntity song = musicRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Canción no encontrada"));
        
        playlist.getCanciones().add(song);
        return playlistRepository.save(playlist);
    }
    
    public PlaylistEntity removeSongFromPlaylist(Long playlistId, Long songId) {
        PlaylistEntity playlist = getPlaylistById(playlistId);
        MusicEntity song = musicRepository.findById(songId)
            .orElseThrow(() -> new RuntimeException("Canción no encontrada"));
        
        playlist.getCanciones().remove(song);
        return playlistRepository.save(playlist);
    }
    
    public Set<MusicEntity> getPlaylistSongs(Long playlistId) {
        PlaylistEntity playlist = getPlaylistById(playlistId);
        return playlist.getCanciones();
    }
    
    public boolean isSongInPlaylist(Long playlistId, Long songId) {
        PlaylistEntity playlist = getPlaylistById(playlistId);
        return playlist.getCanciones().stream()
            .anyMatch(song -> song.getId().equals(songId));
    }
}