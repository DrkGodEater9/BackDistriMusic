package edu.progavud.distrimusic.music;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Inicializador de datos para canciones de ejemplo.
 * 
 * Esta clase se encarga de poblar la base de datos con un conjunto inicial de canciones
 * cuando la aplicación se inicia por primera vez. Solo inserta datos si la tabla de
 * canciones está vacía, evitando duplicados en reinicios posteriores.
 *
 * @author Batapop
 * @author Cabrito
 * @author AlexM
 * @version 1.0
 * @since 2025-07-10
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MusicRepository musicRepository;

    /**
     * Método ejecutado al iniciar la aplicación.
     * Verifica si la base de datos está vacía y, en ese caso, inserta las canciones iniciales.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     * @throws Exception si ocurre un error durante la inicialización
     */
    @Override
    public void run(String... args) throws Exception {
        if (musicRepository.count() == 0) {
            insertarCanciones();
        }
    }

    /**
     * Inserta un conjunto predefinido de canciones populares en la base de datos.
     * Las canciones incluyen éxitos recientes de diversos artistas y géneros.
     */
    private void insertarCanciones() {
        crearCancion("Flowers", "Miley Cyrus", "Endless Summer Vacation",
                "");
        crearCancion("As It Was", "Harry Styles", "Harry's House",
                "");
        crearCancion("Shakira: Bzrp Music Sessions, Vol. 53", "Shakira", "BZRP Music Sessions",
                "");
        crearCancion("Unholy", "Sam Smith ft. Kim Petras", "Gloria",
                "");
        crearCancion("TQG", "Karol G ft. Shakira", "Mañana Será Bonito",
                "");
        crearCancion("La Botella", "Christan Nodal", "Outlaw",
                "");
        crearCancion("Tití Me Preguntó", "Bad Bunny", "Un Verano Sin Ti",
                "");
        crearCancion("Despechá", "Rosalía", "Motomami +",
                "");
        crearCancion("Enemy", "Imagine Dragons", "Mercury - Acts 1 & 2",
                "");
        crearCancion("Heat Waves", "Glass Animals", "Dreamland",
                "");
        crearCancion("Blinding Lights", "The Weeknd", "After Hours",
                "");
        crearCancion("Watermelon Sugar", "Harry Styles", "Fine Line",
                "");

        crearCancion("Levitating", "Dua Lipa", "Future Nostalgia",
                "");

        crearCancion("Save Your Tears", "The Weeknd", "After Hours",
                "");

        crearCancion("Good 4 U", "Olivia Rodrigo", "SOUR",
                "");

        crearCancion("Industry Baby", "Lil Nas X ft. Jack Harlow", "MONTERO",
                "");

        crearCancion("Stay", "The Kid LAROI & Justin Bieber", "F*CK LOVE 3",
                "");

        crearCancion("Anti-Hero", "Taylor Swift", "Midnights",
                "");

        crearCancion("Bad Habit", "Steve Lacy", "Gemini Rights",
                "");

        crearCancion("About Damn Time", "Lizzo", "Special",
                "");
    }

    /**
     * Crea y guarda una nueva canción en la base de datos.
     *
     * @param titulo título de la canción
     * @param artista nombre del artista
     * @param album nombre del álbum
     * @param imageUrl URL de la imagen del álbum (opcional)
     */
    private void crearCancion(String titulo, String artista, String album, String imageUrl) {
        MusicEntity cancion = new MusicEntity();
        cancion.setTitulo(titulo);
        cancion.setArtista(artista);
        cancion.setAlbum(album);
        cancion.setImageUrl(imageUrl);
        musicRepository.save(cancion);
    }
}
