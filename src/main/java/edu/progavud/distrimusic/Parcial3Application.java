package edu.progavud.distrimusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase principal de la aplicación DistriMusic.
 * 
 * Esta clase inicia la aplicación Spring Boot y configura aspectos globales como:
 * - Configuración de CORS para permitir acceso desde el frontend
 * - Inicialización del contexto de Spring
 * - Configuración de seguridad básica
 * 
 * La aplicación implementa una plataforma de música para estudiantes universitarios
 * que permite:
 * - Gestión de usuarios y perfiles
 * - Creación y gestión de playlists
 * - Sistema de comentarios
 * - Gestión de canciones
 * - Sistema de seguimiento entre usuarios
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@SpringBootApplication
public class Parcial3Application {

    /**
     * Método principal que inicia la aplicación Spring Boot.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        SpringApplication.run(Parcial3Application.class, args);
    }

    /**
     * Configura las políticas CORS para permitir el acceso desde otros dominios.
     * 
     * Esta configuración es necesaria para permitir que el frontend Angular/React
     * acceda a los endpoints de la API REST. Configura:
     * - Rutas permitidas: todas ("/**")
     * - Orígenes permitidos: todos ("*")
     * - Métodos HTTP permitidos: GET, POST, PUT, DELETE, OPTIONS
     * - Headers permitidos: todos
     *
     * @return WebMvcConfigurer con la configuración CORS
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")                   // ✅ CORREGIDO: "/**" no "/*"
                        .allowedOrigins("*")                 // Permite cualquier origen
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Métodos permitidos
                        .allowedHeaders("*");                // Permite cualquier encabezado
            }
        };
    }
}