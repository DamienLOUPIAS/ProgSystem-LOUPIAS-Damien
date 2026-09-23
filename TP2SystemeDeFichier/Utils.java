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
               | (((int) (memory[offset] & 0xFF)) << 32);
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
            memory[offset + i] = (byte) ((value >> i * 8) & 0xFF);
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
            resultat = resultat | (((long) (memory[offset] & 0xFF)) << 8 );
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

        for (int i = 0;  i < maxLength || memory[offset + i] == 0; i++) {
            limite = i;
        }

        byte[] aTransposer = new byte[limite];
        
        for (int i = 0; i < limite; i++) {
            aTransposer[i] = memory[offset + i];
        }

        // Lire jusqu'au premier octet nul
        // ou jusqu'à maxLength.

        return new String(aTransposer); 
    }





    /**
     * Tests de base
     */
    public static void testStep1() {

        byte[] buffer = new byte[32];


        System.out.println("=== TEST ÉTAPE 1 : Utils Entiers ===");

        int entier = 0xFE56F478;
        writeInt(buffer, 20, entier);

        assert (readInt(buffer, 20) == entier) : "Etape 1 incorecte (entier)";

        
        short aMemoriser = 678;
        writeShort(buffer, 25, aMemoriser);

        assert (readShort(buffer, 25) == entier) : "Etape 1 incorecte (short)";


        System.out.println("[OK] Étape 1 validée !");
    }



    /**
     * Tests du professeur
     */
    public static void testStep2() {

        byte[] buffer = new byte[32];

        System.out.println("=== TEST ÉTAPE 2 : Utils Entiers ===");


        int value = 0xF0A1B2E3;
        int written = Utils.writeInt(buffer, 3, value);

        assert written == 4 : "writeInt doit retourner 4";

        assert (buffer[3]  & 0xFF) == 0xF0 : "Octet 0 incorrect";
        assert (buffer[4]  & 0xFF) == 0xA1 : "Octet 1 incorrect";
        assert (buffer[5]  & 0xFF) == 0xB2 : "Octet 2 incorrect";
        assert (buffer[6]  & 0xFF) == 0xE3 : "Octet 3 incorrect";

        assert Utils.readInt(buffer, 3) == value :
                "Erreur writeInt / readInt";

        short shortValue = (short) 0xF0A1;
        int shortWritten = Utils.writeShort(buffer, 20, shortValue);

        assert shortWritten == 2 : "writeShort doit retourner 2";

        assert (buffer[20] & 0xFF) == 0xF0 :
                "Premier octet du short incorrect";

        assert (buffer[21] & 0xFF) == 0xA1 :
                "Deuxième octet du short incorrect";

        assert Utils.readShort(buffer, 20) == shortValue :
                "Erreur writeShort / readShort";

        System.out.println("[OK] Étape 2 validée !");
    }


    public static void testStep3() {
        System.out.println("=== TEST ÉTAPE 3 : Utils Long & String ===");

        byte[] buffer = new byte[64];

        long value = 0x1122334455667788L;

        int written = Utils.writeLong(buffer, 0, value);

        assert written == 8 : "writeLong doit retourner 8";

        assert (buffer[0] & 0xFF) == 0x11;
        assert (buffer[1] & 0xFF) == 0x22;
        assert (buffer[2] & 0xFF) == 0x33;
        assert (buffer[3] & 0xFF) == 0x44;
        assert (buffer[4] & 0xFF) == 0x55;
        assert (buffer[5] & 0xFF) == 0x66;
        assert (buffer[6] & 0xFF) == 0x77;
        assert (buffer[7] & 0xFF) == 0x88;

        assert Utils.readLong(buffer, 0) == value :
                "Erreur writeLong / readLong";

        for (int i = 16; i < 32; i++) {
            buffer[i] = (byte) 0x7F;
        }

        int stringWritten =
                Utils.writeString(buffer, 16, "MYFS", 16);

        assert stringWritten == 16 :
                "writeString doit retourner maxLength";

        assert (buffer[16] & 0xFF) == 'M';
        assert (buffer[17] & 0xFF) == 'Y';
        assert (buffer[18] & 0xFF) == 'F';
        assert (buffer[19] & 0xFF) == 'S';

        for (int i = 20; i < 32; i++) {
            assert buffer[i] == 0 :
                    "La zone inutilisée doit être nettoyée";
        }

        assert Utils.readString(buffer, 16, 16).equals("MYFS") :
                "Erreur writeString / readString";

        System.out.println("[OK] Étape 3 validée !");
    }

    /** 
     * Permet l'exécution des tests
     */
    public static void main(String[] args) {
        testStep1();
        testStep2();
        testStep3();
    }

}