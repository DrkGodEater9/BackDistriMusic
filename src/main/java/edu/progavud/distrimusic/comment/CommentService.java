package edu.progavud.distrimusic.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import edu.progavud.distrimusic.persona.UserEntity;
import edu.progavud.distrimusic.persona.UserRepository;
import edu.progavud.distrimusic.playlist.PlaylistEntity;
import edu.progavud.distrimusic.playlist.PlaylistRepository;
import java.util.List;

/**
 * Servicio que gestiona la lógica de negocio relacionada con los comentarios.
 * 
 * Esta clase maneja las operaciones CRUD de comentarios y aplica las reglas de negocio
 * relacionadas con la creación, lectura, actualización y eliminación de comentarios
 * en playlists públicas.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Service
@RequiredArgsConstructor
public class CommentService {
    
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PlaylistRepository playlistRepository;
    
    /**
     * Crea un nuevo comentario en una playlist pública.
     *
     * @param playlistId ID de la playlist donde se creará el comentario
     * @param usuario Nombre de usuario del autor del comentario
     * @param contenido Contenido del comentario
     * @return El comentario creado
     * @throws ResponseStatusException (404) si la playlist o el usuario no existen
     * @throws ResponseStatusException (403) si la playlist no es pública
     */
    public CommentEntity createComment(Long playlistId, String usuario, String contenido) {
        // Verificar que la playlist existe y es pública
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Playlist no encontrada"));
        
        if (!playlist.getEsPublica()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Solo se pueden comentar playlists públicas");
        }
        
        // Verificar que el usuario existe
        UserEntity user = userRepository.findByUsuario(usuario)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        // Crear el comentario
        CommentEntity comment = new CommentEntity(contenido, user, playlist);
        return commentRepository.save(comment);
    }
    
    /**
     * Recupera un comentario por su ID.
     *
     * @param id ID del comentario a buscar
     * @return El comentario encontrado
     * @throws ResponseStatusException (404) si el comentario no existe
     */
    public CommentEntity getCommentById(Long id) {
        return commentRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Comentario no encontrado"));
    }
    
    /**
     * Obtiene todos los comentarios de una playlist pública.
     *
     * @param playlistId ID de la playlist
     * @return Lista de comentarios ordenados por ID descendente
     * @throws ResponseStatusException (404) si la playlist no existe
     * @throws ResponseStatusException (403) si la playlist es privada
     */
    public List<CommentEntity> getCommentsByPlaylist(Long playlistId) {
        // Verificar que la playlist es pública antes de mostrar comentarios
        PlaylistEntity playlist = playlistRepository.findById(playlistId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Playlist no encontrada"));

        if (!playlist.getEsPublica()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "No se pueden ver comentarios de playlists privadas");
        }

        return commentRepository.findByPlaylistIdOrderByIdDesc(playlistId);
    }
    
    /**
     * Obtiene todos los comentarios realizados por un usuario.
     *
     * @param usuario Nombre del usuario
     * @return Lista de comentarios del usuario ordenados por ID descendente
     * @throws ResponseStatusException (404) si el usuario no existe
     */
    public List<CommentEntity> getCommentsByUser(String usuario) {
        // Verificar que el usuario existe
        userRepository.findByUsuario(usuario)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Usuario no encontrado"));
                
        return commentRepository.findByUsuarioUsuarioOrderByIdDesc(usuario);
    }
    
    /**
     * Actualiza el contenido de un comentario existente.
     *
     * @param id ID del comentario a actualizar
     * @param usuario Nombre del usuario que intenta actualizar el comentario
     * @param nuevoContenido Nuevo contenido del comentario
     * @return El comentario actualizado
     * @throws ResponseStatusException (404) si el comentario no existe
     * @throws ResponseStatusException (403) si el usuario no es el autor del comentario
     */
    public CommentEntity updateComment(Long id, String usuario, String nuevoContenido) {
        CommentEntity comment = getCommentById(id);
        
        // Verificar que el usuario es el dueño del comentario
        if (!comment.getUsuario().getUsuario().equals(usuario)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Solo puedes editar tus propios comentarios");
        }
        
        comment.setContenido(nuevoContenido);
        return commentRepository.save(comment);
    }
    
    /**
     * Elimina un comentario.
     *
     * @param id ID del comentario a eliminar
     * @param usuario Nombre del usuario que intenta eliminar el comentario
     * @throws ResponseStatusException (404) si el comentario no existe
     * @throws ResponseStatusException (403) si el usuario no es el autor del comentario ni el dueño de la playlist
     */
    public void deleteComment(Long id, String usuario) {
        CommentEntity comment = getCommentById(id);
        
        // Verificar que el usuario es el dueño del comentario O dueño de la playlist
        boolean isCommentOwner = comment.getUsuario().getUsuario().equals(usuario);
        boolean isPlaylistOwner = comment.getPlaylist().getUsuario().getUsuario().equals(usuario);
        
        if (!isCommentOwner && !isPlaylistOwner) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Solo puedes eliminar tus propios comentarios o comentarios en tus playlists");
        }
        
        commentRepository.deleteById(id);
    }
}