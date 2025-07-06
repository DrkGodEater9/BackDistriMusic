package edu.progavud.distrimusic.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserEntity registerUser(UserEntity user) {
        // Validar que el usuario no exista
        if (userRepository.existsById(user.getUsuario())) {
            throw new RuntimeException("El usuario ya existe");
        }
        
        return userRepository.save(user);
    }
    
    public UserEntity authenticateUser(String usuario, String contraseña) {
        UserEntity user = userRepository.findById(usuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (!user.getContraseña().equals(contraseña)) {
            throw new RuntimeException("Contraseña incorrecta");
        }
        
        return user;
    }
    
    public UserEntity getUserByUsuario(String usuario) {
        return userRepository.findById(usuario)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }
}