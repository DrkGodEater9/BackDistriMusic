package edu.progavud.distrimusic.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    
    List<CommentEntity> findByPlaylistIdOrderByIdDesc(Long playlistId);
    
    List<CommentEntity> findByUsuarioUsuarioOrderByIdDesc(String usuario);
    
    List<CommentEntity> findByPlaylistId(Long playlistId);
    
    void deleteByPlaylistId(Long playlistId);
}