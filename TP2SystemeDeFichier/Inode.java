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
				
		int offset = getInodeOffset() + 28; // debut des pointeurs
		
		

        // Lire les 10 pointeurs directs.
		for (int indice = 0; indice < 10; indice++) {
			pointers[indice] = Utils.readInt(memory, offset);
			offset += 4;
		}

        return pointers;
    }
	
	public void writeToMemory(
        int fileType,
        int fileSize,
        long creationTime,
        long modificationTime,
        int[] directPointers,
        int indirectPointer,
        short permissions,
        int linkCount) {

    byte[] memory =
            memoryManager.getFilesystemMemory();

    int offset = getInodeOffset();
	int curseur = 0;

    // TODO:
    // 1. Numéro d'inode
	
	curseur += Utils.writeInt(memory, offset + curseur, this.inodeNumber);
    // 2. Type
	curseur += Utils.writeInt(memory, offset + curseur, fileType);
    // 3. Taille
	curseur += Utils.writeInt(memory, offset + curseur, fileSize);
    // 4. Création
	curseur += Utils.writeLong(memory, offset + curseur, creationTime);
    // 5. Modification
	curseur += Utils.writeLong(memory, offset + curseur, modificationTime);
    // 6. 10 pointeurs directs
	for (int elt : directPointers) {
		curseur += Utils.writeInt(memory, offset + curseur, elt);
	}
    // 7. Pointeur indirect
	curseur += Utils.writeInt(memory, offset + curseur, indirectPointer);
    // 8. Permissions
	curseur += Utils.writeShort(memory, offset + curseur, permissions);
    // 9. Nombre de liens
	curseur += Utils.writeInt(memory, offset + curseur, linkCount);
	}
}
