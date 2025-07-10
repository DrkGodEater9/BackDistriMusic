package edu.progavud.distrimusic.playlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PlaylistRepository extends JpaRepository<PlaylistEntity, Long> {
    
    List<PlaylistEntity> findByEsPublicaTrue();
    
    List<PlaylistEntity> findByUsuarioUsuario(String usuario);
}