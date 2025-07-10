package edu.progavud.distrimusic.playlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.progavud.distrimusic.music.MusicEntity;
import edu.progavud.distrimusic.music.MusicRepository;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistService {
    
    private final PlaylistRepository playlistRepository;
    private final MusicRepository musicRepository;
    
    @Transactional
    public PlaylistEntity createPlaylist(PlaylistEntity playlist) {
        try {
            log.info("🎵 Creando nueva playlist: {}", playlist.getNombre());
            PlaylistEntity saved = playlistRepository.save(playlist);
            log.info("✅ Playlist creada exitosamente con ID: {}", saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("❌ Error al crear playlist: ", e);
            throw new RuntimeException("Error al crear playlist: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public PlaylistEntity getPlaylistById(Long id) {
        log.info("🔍 Buscando playlist con ID: {}", id);
        Optional<PlaylistEntity> playlist = playlistRepository.findById(id);
        
        if (playlist.isPresent()) {
            log.info("✅ Playlist encontrada: {}", playlist.get().getNombre());
            return playlist.get();
        } else {
            log.warn("⚠️ Playlist no encontrada con ID: {}", id);
            throw new RuntimeException("Playlist no encontrada con ID: " + id);
        }
    }
    
    @Transactional(readOnly = true)
    public List<PlaylistEntity> getAllPlaylists() {
        try {
            log.info("📋 Obteniendo todas las playlists");
            List<PlaylistEntity> playlists = playlistRepository.findAll();
            log.info("✅ Se encontraron {} playlists", playlists.size());
            return playlists;
        } catch (Exception e) {
            log.error("❌ Error al obtener todas las playlists: ", e);
            throw new RuntimeException("Error al obtener playlists: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public List<PlaylistEntity> getPublicPlaylists() {
        try {
            log.info("🌍 Obteniendo playlists públicas");
            List<PlaylistEntity> playlists = playlistRepository.findByEsPublicaTrue();
            log.info("✅ Se encontraron {} playlists públicas", playlists.size());
            return playlists;
        } catch (Exception e) {
            log.error("❌ Error al obtener playlists públicas: ", e);
            throw new RuntimeException("Error al obtener playlists públicas: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public List<PlaylistEntity> getPlaylistsByUser(String usuario) {
        try {
            log.info("👤 Obteniendo playlists del usuario: {}", usuario);
            List<PlaylistEntity> playlists = playlistRepository.findByUsuarioUsuario(usuario);
            log.info("✅ Se encontraron {} playlists para el usuario {}", playlists.size(), usuario);
            return playlists;
        } catch (Exception e) {
            log.error("❌ Error al obtener playlists del usuario {}: ", usuario, e);
            throw new RuntimeException("Error al obtener playlists del usuario: " + e.getMessage());
        }
    }
    
    @Transactional
    public PlaylistEntity updatePlaylist(Long id, PlaylistEntity playlistRequest) {
        try {
            log.info("📝 Actualizando playlist con ID: {}", id);
            PlaylistEntity existingPlaylist = getPlaylistById(id);
            
            if (playlistRequest.getNombre() != null && !playlistRequest.getNombre().trim().isEmpty()) {
                log.info("📝 Actualizando nombre: {} -> {}", existingPlaylist.getNombre(), playlistRequest.getNombre());
                existingPlaylist.setNombre(playlistRequest.getNombre().trim());
            }
            if (playlistRequest.getEsPublica() != null) {
                log.info("🔒 Actualizando privacidad: {} -> {}", existingPlaylist.getEsPublica(), playlistRequest.getEsPublica());
                existingPlaylist.setEsPublica(playlistRequest.getEsPublica());
            }
            if (playlistRequest.getImageUrl() != null) {
                log.info("🖼️ Actualizando imagen URL");
                existingPlaylist.setImageUrl(playlistRequest.getImageUrl());
            }
            
            PlaylistEntity saved = playlistRepository.save(existingPlaylist);
            log.info("✅ Playlist actualizada exitosamente");
            return saved;
        } catch (Exception e) {
            log.error("❌ Error al actualizar playlist {}: ", id, e);
            throw new RuntimeException("Error al actualizar playlist: " + e.getMessage());
        }
    }
    
    @Transactional
    public void deletePlaylist(Long id) {
        try {
            log.info("🗑️ Eliminando playlist con ID: {}", id);
            if (!playlistRepository.existsById(id)) {
                log.warn("⚠️ Playlist no existe con ID: {}", id);
                throw new RuntimeException("Playlist no encontrada con ID: " + id);
            }
            playlistRepository.deleteById(id);
            log.info("✅ Playlist eliminada exitosamente");
        } catch (Exception e) {
            log.error("❌ Error al eliminar playlist {}: ", id, e);
            throw new RuntimeException("Error al eliminar playlist: " + e.getMessage());
        }
    }
    
    // ✅ Métodos para manejar canciones con logs detallados
    
    @Transactional
    public PlaylistEntity addSongToPlaylist(Long playlistId, Long songId) {
        log.info("🎵 Intentando agregar canción {} a playlist {}", songId, playlistId);
        
        try {
            // Verificar que ambos IDs sean válidos
            if (playlistId == null || playlistId <= 0) {
                log.error("❌ ID de playlist inválido: {}", playlistId);
                throw new RuntimeException("ID de playlist debe ser un número positivo");
            }
            
            if (songId == null || songId <= 0) {
                log.error("❌ ID de canción inválido: {}", songId);
                throw new RuntimeException("ID de canción debe ser un número positivo");
            }
            
            // Buscar playlist
            log.info("🔍 Buscando playlist...");
            PlaylistEntity playlist = getPlaylistById(playlistId);
            log.info("✅ Playlist encontrada: {} (Usuario: {})", playlist.getNombre(), playlist.getNombreUsuario());
            
            // Buscar canción
            log.info("🔍 Buscando canción...");
            Optional<MusicEntity> songOpt = musicRepository.findById(songId);
            if (!songOpt.isPresent()) {
                log.error("❌ Canción no encontrada con ID: {}", songId);
                throw new RuntimeException("Canción no encontrada con ID: " + songId);
            }
            
            MusicEntity song = songOpt.get();
            log.info("✅ Canción encontrada: {} - {}", song.getTitulo(), song.getArtista());
            
            // Verificar si la canción ya está en la playlist
            boolean alreadyExists = playlist.getCanciones().stream()
                .anyMatch(c -> c.getId().equals(songId));
                
            if (alreadyExists) {
                log.warn("⚠️ La canción ya está en la playlist");
                throw new RuntimeException("La canción ya está en la playlist");
            }
            
            // Agregar canción
            log.info("➕ Agregando canción a la playlist...");
            playlist.getCanciones().add(song);
            
            // Guardar cambios
            log.info("💾 Guardando cambios...");
            PlaylistEntity savedPlaylist = playlistRepository.save(playlist);
            
            log.info("✅ Canción agregada exitosamente. Total de canciones: {}", savedPlaylist.getCantidadCanciones());
            return savedPlaylist;
            
        } catch (RuntimeException e) {
            log.error("❌ Error de negocio: {}", e.getMessage());
            throw e; // Re-lanzar errores de negocio
        } catch (Exception e) {
            log.error("❌ Error inesperado al agregar canción {} a playlist {}: ", songId, playlistId, e);
            throw new RuntimeException("Error interno al agregar canción: " + e.getMessage());
        }
    }
    
    @Transactional
    public PlaylistEntity removeSongFromPlaylist(Long playlistId, Long songId) {
        log.info("🗑️ Intentando remover canción {} de playlist {}", songId, playlistId);
        
        try {
            PlaylistEntity playlist = getPlaylistById(playlistId);
            
            Optional<MusicEntity> songOpt = musicRepository.findById(songId);
            if (!songOpt.isPresent()) {
                throw new RuntimeException("Canción no encontrada con ID: " + songId);
            }
            
            MusicEntity song = songOpt.get();
            
            // Verificar si la canción está en la playlist
            boolean exists = playlist.getCanciones().stream()
                .anyMatch(c -> c.getId().equals(songId));
                
            if (!exists) {
                log.warn("⚠️ La canción no está en la playlist");
                throw new RuntimeException("La canción no está en la playlist");
            }
            
            // Remover canción
            playlist.getCanciones().removeIf(c -> c.getId().equals(songId));
            
            PlaylistEntity savedPlaylist = playlistRepository.save(playlist);
            log.info("✅ Canción eliminada exitosamente");
            return savedPlaylist;
            
        } catch (RuntimeException e) {
            log.error("❌ Error de negocio: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Error inesperado: ", e);
            throw new RuntimeException("Error interno: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public Set<MusicEntity> getPlaylistSongs(Long playlistId) {
        try {
            log.info("📋 Obteniendo canciones de playlist {}", playlistId);
            PlaylistEntity playlist = getPlaylistById(playlistId);
            Set<MusicEntity> canciones = playlist.getCanciones();
            // Forzar la carga lazy
            canciones.size();
            log.info("✅ Se encontraron {} canciones", canciones.size());
            return canciones;
        } catch (Exception e) {
            log.error("❌ Error al obtener canciones de playlist {}: ", playlistId, e);
            throw new RuntimeException("Error al obtener canciones: " + e.getMessage());
        }
    }
    
    @Transactional(readOnly = true)
    public boolean isSongInPlaylist(Long playlistId, Long songId) {
        try {
            log.info("🔍 Verificando si canción {} está en playlist {}", songId, playlistId);
            PlaylistEntity playlist = getPlaylistById(playlistId);
            boolean exists = playlist.getCanciones().stream()
                .anyMatch(song -> song.getId().equals(songId));
            log.info("✅ Resultado: {}", exists ? "SÍ está" : "NO está");
            return exists;
        } catch (Exception e) {
            log.error("❌ Error al verificar: ", e);
            return false;
        }
    }
}