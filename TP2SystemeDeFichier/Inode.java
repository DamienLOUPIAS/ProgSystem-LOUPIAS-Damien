public class Inode {

    private MemoryManager memoryManager;
    private int inodeNumber;

    public static final int INODE_SIZE = 128;
    public static final int DIRECT_POINTERS = 10;

    public Inode(
            MemoryManager memoryManager,
            int inodeNumber) {

        this.memoryManager = memoryManager;
        this.inodeNumber = inodeNumber;
    }

    public int getInodeOffset() {
        // Calculer l'offset exact de l'inode.
        return MemoryManager.INODE_TABLE_OFFSET + inodeNumber * INODE_SIZE;
    }

    public int getFileType() {
        // Lire le type à offset + 4.
        return Utils.readInt(memoryManager.getFilesystemMemory(), this.getInodeOffset() + 4);
    }

    public int getFileSize() {
        // Lire la taille à offset + 8.
        return Utils.readInt(memoryManager.getFilesystemMemory(), this.getInodeOffset() + 8);
    }

    public int[] getDirectPointers() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        int[] pointers =
                new int[DIRECT_POINTERS];
				
		int offset = getInodeOffset() + 27; // debut des pointeurs
		int indice = 0;
		

        // Lire les 10 pointeurs directs.
		for (; offset < 67; offset += 4) {
			pointers[indice] = Utils.readInt(memory, offset);
			indice++;
		}

        return pointers;
    }
}
