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
            "https://i.scdn.co/image/ab67616d00001e02f4e47edfa135929c4cf8b85f");

        crearCancion("As It Was", "Harry Styles", "Harry's House", 
            "https://i.scdn.co/image/ab67616d00001e022e8ed79e177ff6011076f5f0");

        crearCancion("Shakira: Bzrp Music Sessions, Vol. 53", "Shakira", "BZRP Music Sessions", 
            "https://i.scdn.co/image/ab67616d00001e02825ad71d587c8a86b40e2874");

        crearCancion("Unholy", "Sam Smith ft. Kim Petras", "Gloria", 
            "https://i.scdn.co/image/ab67616d00001e02c0235e5c7a0bf5ad81374e68");

        crearCancion("TQG", "Karol G ft. Shakira", "Mañana Será Bonito", 
            "https://i.scdn.co/image/ab67616d00001e02df2d0539e43b91d992f0c2c3");

        crearCancion("La Botella", "Christan Nodal", "Outlaw", 
            "https://i.scdn.co/image/ab67616d00001e02a46ced2bb6a5b9e4b72a2c9e");

        crearCancion("Tití Me Preguntó", "Bad Bunny", "Un Verano Sin Ti", 
            "https://i.scdn.co/image/ab67616d00001e02c0c267651c8d26cedc50a6be");

        crearCancion("Despechá", "Rosalía", "Motomami +", 
            "https://i.scdn.co/image/ab67616d00001e02e85259a1cae29a8d91f2093d");

        crearCancion("Enemy", "Imagine Dragons", "Mercury - Acts 1 & 2", 
            "https://i.scdn.co/image/ab67616d00001e027dd8f95320e8ef08aa121dfe");

        crearCancion("Heat Waves", "Glass Animals", "Dreamland", 
            "https://i.scdn.co/image/ab67616d00001e02e6732d25b2a8f2a7a22923d3");

        crearCancion("Blinding Lights", "The Weeknd", "After Hours", 
            "https://i.scdn.co/image/ab67616d00001e028863bc11d2aa12b54f5aeb36");

        crearCancion("Watermelon Sugar", "Harry Styles", "Fine Line", 
            "https://i.scdn.co/image/ab67616d00001e02adea3a07aba0a3627e71b0de");

        crearCancion("Levitating", "Dua Lipa", "Future Nostalgia", 
            "https://i.scdn.co/image/ab67616d00001e023ba82abb1a87bf65b1bb7bb5");

        crearCancion("Save Your Tears", "The Weeknd", "After Hours", 
            "https://i.scdn.co/image/ab67616d00001e028863bc11d2aa12b54f5aeb36");

        crearCancion("Good 4 U", "Olivia Rodrigo", "SOUR", 
            "https://i.scdn.co/image/ab67616d00001e02a91c10fe9472d9bd89802e5a");

        crearCancion("Industry Baby", "Lil Nas X ft. Jack Harlow", "MONTERO", 
            "https://i.scdn.co/image/ab67616d00001e02be82673b5f79d9658ec0a9fd");

        crearCancion("Stay", "The Kid LAROI & Justin Bieber", "F*CK LOVE 3", 
            "https://i.scdn.co/image/ab67616d00001e022928b933eefd7a9e49aee307");

        crearCancion("Anti-Hero", "Taylor Swift", "Midnights", 
            "https://i.scdn.co/image/ab67616d00001e02bb54dde68cd23e2a268ae0f5");

        crearCancion("Bad Habit", "Steve Lacy", "Gemini Rights", 
            "https://i.scdn.co/image/ab67616d00001e02b85259a5d79a82d6e2f5bf95");

        crearCancion("About Damn Time", "Lizzo", "Special", 
            "https://i.scdn.co/image/ab67616d00001e02c536e4edb2de1c0bb42bbcaa");
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