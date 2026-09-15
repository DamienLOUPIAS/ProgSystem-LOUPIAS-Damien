import java.io.FileWriter;
import java.io.IOException;

public class Image {
	
	private final static int VALEUR_MAX = 255;

    private int width;
    private int height;
    // pixels[y][x][0=R,1=G,2=B]
    private int[][][] pixels; // pixels[y][x][0=R,1=G,2=B]

    public int getWidth() { return width; }
    public int getHeight() { return height; }
	public int getValeurMax() { return VALEUR_MAX; }

    /**
     * Constructeur : initialise une image vide.
     */
    public Image(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[height][width][3];
    }
	
	/**
     * Définit la couleur d'un pixel à la position (x, y)
     */
    public void setPixel(int x, int y, int r, int g, int b) {
        if (x >= 0 && x < width && y >= 0 && y < height) {
            pixels[y][x][0] = r;
            pixels[y][x][1] = g;
            pixels[y][x][2] = b;
        }
    }
	
    /**
     * Sauvegarde l'image au format texte PPM (P3)
     */
    public void save_txt(String filename) throws IOException {
		
		 try {
            FileWriter writer = new FileWriter(filename);

           

            writer.write("P3\n");
            // Écriture des dimensions
            writer.write(this.getWidth() + " " + this.getHeight() + "\n");
            // Écriture de la valeur maximal

            // Écriture des pixels
            writer.write(VALEUR_MAX + "\n");
			
			// Ecriture dans le fichier
			for (int py = 0; py < this.height; py++) {
				// Génère les lignes
				for (int px = 0; px < this.width; px++) {
				// Génère les pixels
					for (int valColor = 0; valColor <= 2; valColor++) {
						// Génère les différente couleur / Pas optimum car on appelle plusieurs fois write
						writer.write(this.pixels[py][px][valColor] + " ");
						
					}				
				}
				writer.write("\n");
			}

           

            writer.close(); // Fermeture du fichier

        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }
	
	
	/**
     * Sauvegarde l'image au format texte PPM (P3)
     */
    public void save_binaire(String filename) throws IOException {
		
		 try {
            FileWriter writer = new FileWriter(filename);
			
			//tableau de byte contenant les bonnes valeur pour construire l'image.
			byte[] aRetourner = new byte[this.getWidth()*this.getHeight()*3];
			
			// Curseur qui nous permet "d'écrire" dans le tableau comme on a fait précédement avec writer
			int curseur = 0;
			
			int = passage;
		   
			// header
            writer.write("P6\n");
            // Écriture des dimensions
            writer.write(this.getWidth() + " " + this.getHeight() + "\n");
            // Écriture de la valeur maximal

            // Écriture des pixels
            writer.write(VALEUR_MAX + "\n");
			
			// Ecriture dans le fichier
			for (int py = 0; py < this.height; py++) {
				// Génère les lignes
				for (int px = 0; px < this.width; px++) {
				// Génère les pixels
					for (int valColor = 0; valColor <= 2; valColor++) {
						// Génère les différente couleur 
						passage = this.pixels[py][px][valColor];
						aRetourner[curseur] = (byte) (passage 0xFF)
						curseur++;
						
					}				
				}
			}
			
			
			writer.close(); // Fermeture du fichier
			
			
			try (FileOutputStream fos = new FileOutputStream(filename)) {
                 
           
				fos.write(aRetourner);

				System.out.println("Data successfully written to the file.");
			}
			catch (IOException e) {
				System.out.println("An error occurred: ");
			}
			

           

        

        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture du fichier : " + e.getMessage());
        }
    }
}