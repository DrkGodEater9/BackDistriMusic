package edu.progavud.distrimusic.user;

import edu.progavud.distrimusic.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    
}