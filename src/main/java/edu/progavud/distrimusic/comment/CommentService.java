package edu.progavud.distrimusic.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import edu.progavud.distrimusic.persona.UserEntity;
import edu.progavud.distrimusic.persona.UserRepository;
import edu.progavud.distrimusic.playlist.PlaylistEntity;
import edu.progavud.distrimusic.playlist.PlaylistRepository;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    
    public CommentEntity createComment(Long playlistId, String usuario, String contenido) {
        // Verificar que la playlist existe y es pública
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));
        
        if (!playlist.getEsPublica()) {
            throw new RuntimeException("Solo se pueden comentar playlists públicas");
        }
        
        // Verificar que el usuario existe
        UserEntity user = userRepository.findByUsuario(usuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Crear el comentario
        CommentEntity comment = new CommentEntity(contenido, user, playlist);
        return commentRepository.save(comment);
    }
    
    public CommentEntity getCommentById(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Comentario no encontrado"));
    }
    
    public List<CommentEntity> getCommentsByPlaylist(Long playlistId) {
        // Verificar que la playlist es pública antes de mostrar comentarios
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new RuntimeException("Playlist no encontrada"));
        
        if (!playlist.getEsPublica()) {
            throw new RuntimeException("No se pueden ver comentarios de playlists privadas");
        }
        
        return commentRepository.findByPlaylistIdOrderByIdDesc(playlistId);
    }
    
    public List<CommentEntity> getCommentsByUser(String usuario) {
        return commentRepository.findByUsuarioUsuarioOrderByIdDesc(usuario);
    }
    
    public CommentEntity updateComment(Long id, String usuario, String nuevoContenido) {
        CommentEntity comment = getCommentById(id);
        
        // Verificar que el usuario es el dueño del comentario
        if (!comment.getUsuario().getUsuario().equals(usuario)) {
            throw new RuntimeException("Solo puedes editar tus propios comentarios");
        }
        
        comment.setContenido(nuevoContenido);
        return commentRepository.save(comment);
    }
    
    public void deleteComment(Long id, String usuario) {
        CommentEntity comment = getCommentById(id);
        
        // Verificar que el usuario es el dueño del comentario O dueño de la playlist
        boolean isCommentOwner = comment.getUsuario().getUsuario().equals(usuario);
        boolean isPlaylistOwner = comment.getPlaylist().getUsuario().getUsuario().equals(usuario);
        
        if (!isCommentOwner && !isPlaylistOwner) {
            throw new RuntimeException("Solo puedes eliminar tus propios comentarios o comentarios en tus playlists");
        }
        
        commentRepository.deleteById(id);
    }
}