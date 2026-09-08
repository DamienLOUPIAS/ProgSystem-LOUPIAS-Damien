import java.io.FileWriter;
import java.io.IOException;

public class Image {
    public static void main(String[] args) {

        private final int LARGEUR = 3;
        private final int HAUTEUR = 2;
        private final int VALEUR_MAX = 255;

        private int width;
        private int height;
        // pixels[y][x][0=R,1=G,2=B]
        private int[][][] pixels; // pixels[y][x][0=R,1=G,2=B]

        public int getWidth() { return width; }
        public int getHeight() { return height; }


        try {
            FileWriter writer = new FileWriter("FirstPPM.ppm");

           

            writer.write("P3\n");
            // Écriture des dimensions
            writer.write(LARGEUR + " " + HAUTEUR);
            // Écriture de la valeur maximal

            // Écriture des pixels
            writer.write(VALEUR_MAX);

            // Première ligne : rouge, vert, bleu
            writer.write("255 0 0 0 255 0 0 0 255");

            // Deuxième ligne : jaune, blanc, noir
            writer.write("255 255 0 255 255 255 0 0 0");

            writer.close(); // Fermeture du fichier

            System.out.println("Image PPM créée avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }
}