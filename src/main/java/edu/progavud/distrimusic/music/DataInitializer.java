package edu.progavud.distrimusic.music;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MusicRepository musicRepository;

    @Override
    public void run(String... args) throws Exception {
        if (musicRepository.count() == 0) {
            insertarCanciones();
        }
    }

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

    private void crearCancion(String titulo, String artista, String album, String imageUrl) {
        MusicEntity cancion = new MusicEntity();
        cancion.setTitulo(titulo);
        cancion.setArtista(artista);
        cancion.setAlbum(album);
        cancion.setImageUrl(imageUrl);
        musicRepository.save(cancion);
    }
}
