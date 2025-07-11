package edu.progavud.distrimusic.playlist;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.progavud.distrimusic.music.MusicEntity;
import edu.progavud.distrimusic.music.MusicRepository;
import edu.progavud.distrimusic.comment.CommentEntity;
import edu.progavud.distrimusic.comment.CommentRepository;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

/**
 * Servicio que implementa la lógica de negocio relacionada con las playlists.
 *
 * Esta clase maneja todas las operaciones relacionadas con playlists, incluyendo:
 * - Creación y gestión de playlists
 * - Manejo de canciones dentro de las playlists
 * - Gestión de privacidad (pública/privada)
 *
 * Implementa manejo seguro de colecciones para evitar ConcurrentModificationException
 * y optimizaciones para el manejo de lazy loading.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MusicRepository musicRepository;
    private final CommentRepository commentRepository; // ✅ Se agregó correctamente

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

            Optional<PlaylistEntity> playlistOpt = playlistRepository.findById(id);
            if (!playlistOpt.isPresent()) {
                log.warn("⚠️ Playlist no existe con ID: {}", id);
                throw new RuntimeException("Playlist no encontrada con ID: " + id);
            }

            PlaylistEntity playlist = playlistOpt.get();
            log.info("📋 Eliminando playlist: {} del usuario: {}", playlist.getNombre(), playlist.getNombreUsuario());

            // 1. Eliminar comentarios asociados
            log.info("💬 Eliminando comentarios asociados...");
            try {
                List<CommentEntity> comments = commentRepository.findByPlaylistId(id);
                if (!comments.isEmpty()) {
                    log.info("📝 Encontrados {} comentarios para eliminar", comments.size());
                    commentRepository.deleteAll(comments);
                    commentRepository.flush();
                    log.info("✅ Comentarios eliminados exitosamente");
                } else {
                    log.info("ℹ️ No hay comentarios para eliminar");
                }
            } catch (Exception e) {
                log.error("❌ Error eliminando comentarios: {}", e.getMessage());
            }

            // 2. Limpiar relaciones Many-to-Many con canciones
            log.info("🔗 Limpiando relaciones con canciones...");
            try {
                if (playlist.getCanciones() != null && !playlist.getCanciones().isEmpty()) {
                    log.info("🎵 Encontradas {} canciones para desasociar", playlist.getCanciones().size());
                    playlist.getCanciones().clear();
                    playlistRepository.save(playlist);
                    playlistRepository.flush();
                    log.info("✅ Relaciones con canciones eliminadas");
                } else {
                    log.info("ℹ️ No hay canciones para desasociar");
                }
            } catch (Exception e) {
                log.error("❌ Error limpiando canciones: {}", e.getMessage());
            }

            // 3. Eliminar la playlist
            log.info("🗑️ Eliminando registro de playlist...");
            playlistRepository.deleteById(id);
            playlistRepository.flush();
            log.info("✅ Playlist eliminada exitosamente");

        } catch (RuntimeException e) {
            log.error("❌ Error de negocio al eliminar playlist {}: {}", id, e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Error técnico al eliminar playlist {}: ", id, e);
            throw new RuntimeException("Error interno al eliminar playlist: " + e.getMessage());
        }
    }

    @Transactional
    public PlaylistEntity addSongToPlaylist(Long playlistId, Long songId) {
        log.info("🎵 Intentando agregar canción {} a playlist {}", songId, playlistId);

        try {
            if (playlistId == null || playlistId <= 0) {
                throw new RuntimeException("ID de playlist debe ser un número positivo");
            }

            if (songId == null || songId <= 0) {
                throw new RuntimeException("ID de canción debe ser un número positivo");
            }

            boolean alreadyExists = playlistRepository.existsByIdAndCancionesId(playlistId, songId);
            if (alreadyExists) {
                throw new RuntimeException("La canción ya está en la playlist");
            }

            PlaylistEntity playlist = getPlaylistById(playlistId);

            Optional<MusicEntity> songOpt = musicRepository.findById(songId);
            if (!songOpt.isPresent()) {
                throw new RuntimeException("Canción no encontrada con ID: " + songId);
            }

            MusicEntity song = songOpt.get();

            Set<MusicEntity> canciones = playlist.getCanciones();
            if (canciones == null) {
                canciones = new HashSet<>();
                playlist.setCanciones(canciones);
            }

            Set<MusicEntity> newCanciones = new HashSet<>(canciones);
            newCanciones.add(song);
            playlist.setCanciones(newCanciones);

            PlaylistEntity savedPlaylist = playlistRepository.save(playlist);
            playlistRepository.flush();

            log.info("✅ Canción agregada exitosamente. Total de canciones: {}", savedPlaylist.getCantidadCanciones());
            return savedPlaylist;

        } catch (RuntimeException e) {
            log.error("❌ Error de negocio: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("❌ Error inesperado al agregar canción {} a playlist {}: ", songId, playlistId, e);
            throw new RuntimeException("Error interno al agregar canción: " + e.getMessage());
        }
    }

    @Transactional
    public PlaylistEntity removeSongFromPlaylist(Long playlistId, Long songId) {

        try {
            PlaylistEntity playlist = getPlaylistById(playlistId);

            Optional<MusicEntity> songOpt = musicRepository.findById(songId);
            if (!songOpt.isPresent()) {
                throw new RuntimeException("Canción no encontrada con ID: " + songId);
            }

            MusicEntity song = songOpt.get();

            boolean exists = playlist.getCanciones().stream()
                .anyMatch(c -> c.getId().equals(songId));

            if (!exists) {
                throw new RuntimeException("La canción no está en la playlist");
            }

            Set<MusicEntity> canciones = playlist.getCanciones();
            Set<MusicEntity> newCanciones = new HashSet<>(canciones);
            newCanciones.removeIf(c -> c.getId().equals(songId));
            playlist.setCanciones(newCanciones);

            PlaylistEntity savedPlaylist = playlistRepository.save(playlist);
            playlistRepository.flush();

            return savedPlaylist;

        } catch (RuntimeException e) {

            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Set<MusicEntity> getPlaylistSongs(Long playlistId) {
        try {
            PlaylistEntity playlist = getPlaylistById(playlistId);
            Set<MusicEntity> canciones = playlist.getCanciones();
            canciones.size(); // Forzar carga
            return canciones;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener canciones: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public boolean isSongInPlaylist(Long playlistId, Long songId) {
        try {
            return playlistRepository.existsByIdAndCancionesId(playlistId, songId);
        } catch (Exception e) {
            return false;
        }
    }
}
