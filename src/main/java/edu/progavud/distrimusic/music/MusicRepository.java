package edu.progavud.distrimusic.music;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MusicRepository extends JpaRepository<MusicEntity, Long> {
    
    List<MusicEntity> findByTituloContainingIgnoreCaseOrArtistaContainingIgnoreCase(String titulo, String artista);
    
    List<MusicEntity> findByArtistaContainingIgnoreCase(String artista);
    
    List<MusicEntity> findByAlbumContainingIgnoreCase(String album);
    
    List<MusicEntity> findAllByOrderByTituloAsc();
}