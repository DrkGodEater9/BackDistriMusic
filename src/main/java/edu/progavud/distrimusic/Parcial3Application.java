package edu.progavud.distrimusic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class Parcial3Application {

	public static void main(String[] args) {
		SpringApplication.run(Parcial3Application.class, args);
	}

	@Bean
	// Método para configurar el acceso al proyecto desde otros dominios o puertos
	// Este método permite resolver el problema con las políticas CORS de los navegadores
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