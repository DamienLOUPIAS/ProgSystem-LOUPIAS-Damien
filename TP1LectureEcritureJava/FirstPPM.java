import java.io.FileWriter;
import java.io.IOException;

public class FirstPPM {
    public static void main(String[] args) {

        final int LARGEUR = 3;
        final int HAUTEUR = 2;
        final int VALEUR_MAX = 255;

        try {
            FileWriter writer = new FileWriter("FirstPPM.ppm");

           

            writer.write("P3\n");
            // Écriture des dimensions
            writer.write(LARGEUR + " " + HAUTEUR + "\n");
            // Écriture de la valeur maximal

            // Écriture des pixels
            writer.write(VALEUR_MAX + "\n");

            // Première ligne : rouge, vert, bleu
            writer.write("255 0 0 0 255 0 0 0 255\n");

            // Deuxième ligne : jaune, blanc, noir
            writer.write("255 255 0 255 255 255 0 0 0\n");

            writer.close(); // Fermeture du fichier

            System.out.println("Image PPM créée avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }
}