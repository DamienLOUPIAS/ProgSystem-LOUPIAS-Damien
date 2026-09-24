import java.util.List;

/**
 * Classe utilitaire simulant un outil de gestion de mémoire 
 * La mémoire est représentée par un tableau de bytes
 * @author Damien LOUPIAS
 */
public class Utils {

    /**
     * Ecrit un int dans la mémoire.
     * @param memory la memoire dans laquelle écrire l'entier
     * @param offset l'adresse a laquelle écrire l'entier (de offset à offset + 3)
     * @param value l'entier a écrire en mémoire.
     */
    public static int writeInt(byte[] memory, int offset, int value) {
        // On ne vérifie pas si offset > taille(memoire) car l'erreur est gérée directement par le systeme
        memory[offset + 3] = (byte) (value & 0xFF);
        memory[offset + 2] = (byte) ((value >> 8) & 0xFF);
        memory[offset + 1] = (byte) ((value >> 16) & 0xFF);
        memory[offset] = (byte) ((value >> 24) & 0xFF);
        return 4; // Correspond a la place prise par l'entier, pour eventuellement incrémenter un curseur.
    }

    /**
     * @param memory la memoire dans laquelle écrire l'entier
     * @param offset l'adresse a laquelle récupérer l'entier (de offset à offset + 3)
     * @return l'entier inscrit de offset à offset + 3 au format big indian
     */
    public static int readInt(byte[] memory, int offset) {
        // Utilisation du | a la place du + pour éviter de perdre tu temps CPU
        return ((int)(memory[offset+ 3] & 0xFF))
               | (((int)(memory[offset + 2] & 0xFF)) << 8)
               | (((int)(memory[offset + 1] & 0xFF)) << 16)
               | (((int) (memory[offset] & 0xFF)) << 24);
    }

    /**
     * Ecrit un short dans la mémoire.
     * @param memory la memoire dans laquelle écrire le short
     * @param offset l'adresse a laquelle écrire le short (de offset à offset + 1)
     * @param value le short a écrire en mémoire.
     */
    public static int writeShort(byte[] memory, int offset, short value) {
        memory[offset + 1] = (byte) (value & 0xFF);
        memory[offset] = (byte) ((value >> 8) & 0xFF);
        return 2;
    }


    /**
     * @param memory la memoire dans laquelle écrire le short
     * @param offset l'adresse a laquelle récupérer le short (de offset à offset + 3)
     * @return le short inscrit de offset à offset + 1 au format big indian
     */
    public static short readShort(byte[] memory, int offset) {
        // Le premier cast de short vient du fait que | convertit implicitement en int.
        return (short) (((short)(memory[offset + 1] & 0xFF)) 
               | (((short) (memory[offset] & 0xFF)) << 8 ));
    }


    /*
        Partie String ----------------------------------------------------------------------------------    
    */

  
    /**
     * Ecrit un long dans la mémoire.
     * @param memory la memoire dans laquelle écrire le long
     * @param offset l'adresse a laquelle écrire le long (de offset à offset + 1)
     * @param value le long a écrire en mémoire.
     */      
    public static int writeLong(byte[] memory, int offset, long value) {
        for (int i = 0; i < 8; i++) {
            memory[offset + (7 - i)] = (byte) ((value >> i * 8) & 0xFF);
        }
        return 8;
    }

    /**
     * @param memory la memoire dans laquelle écrire le long
     * @param offset l'adresse a laquelle récupérer le long (de offset à offset + 3)
     * @return le long inscrit de offset à offset + 1 au format big indian
     */
    public static long readLong(byte[] memory, int offset) {
        long resultat = 0;
        for (int i = 0; i < 8; i++) {
            resultat = resultat | (((long) (memory[offset + (7 - i)] & 0xFF)) << (8 * i) );
        }
        return resultat;
    }


    
    /**
     * Ecrit un String dans la mémoire.
     * @param memory la memoire dans laquelle écrire le String
     * @param offset l'adresse a laquelle écrire le String (de offset à offset + 1)
     * @param value le String a écrire en mémoire.
     */
    public static int writeString(
            byte[] memory,
            int offset,
            String str,
            int maxLength) {

        byte[] aEcrire;
        int arret = 0; // init pour le compilateur

        // 1. Convertir la chaîne en octets.
        aEcrire = str.getBytes();
        // 2. Copier les octets sans dépasser maxLength.
        if (aEcrire.length <= maxLength) {
            arret = aEcrire.length;
        } else {
            arret = maxLength;
        }
        for (int i = 0; i < arret; i++) {
            memory[offset + i] = aEcrire[i];
        }
        // 3. Nettoyer le reste de la zone avec des zéros.
        if (arret != maxLength) { // Condition supplémentaire mais evite du code en plus
            for (int i = arret; i < maxLength; i++) {
                memory[offset + i] = 0; 
            }
        }

        return maxLength;
    }

    /**
     * @param memory la memoire dans laquelle écrire le String
     * @param offset l'adresse a laquelle récupérer le String (de offset à offset + 3)
     * @return le String inscrit de offset à offset + 1 au format big indian
     */
    public static String readString(
            byte[] memory,
            int offset,
            int maxLength) {

        int limite = 0; // init pour compilateur

        for (int i = 0;  i < maxLength && memory[offset + i] != 0; i++) {
            limite = i + 1;
        }

        byte[] aTransposer = new byte[limite];
        
        for (int i = 0; i < limite; i++) {
            aTransposer[i] = memory[offset + i];
        }

        // Lire jusqu'au premier octet nul
        // ou jusqu'à maxLength.

        return new String(aTransposer); 
    }






}