import java.util.*;

public class VirtualFileSystem {

    private MemoryManager memoryManager;

    public VirtualFileSystem() {
        this.memoryManager =
                new MemoryManager();
        initialisation();
    }
	
	private void initialisation() {
        int offset;
		for (int i = 0; i < MemoryManager.MAX_INODES; i++) {
            offset = MemoryManager.INODE_TABLE_OFFSET + i * Inode.INODE_SIZE;
            Utils.writeInt(memoryManager.getFilesystemMemory(), offset, -1);
        }
	}

    private int allocateInode() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        // Parcourir les inodes de 0 à MAX_INODES - 1.
		int i = 0;
		while (i < MemoryManager.MAX_INODES && Utils.readInt(memory, MemoryManager.INODE_TABLE_OFFSET + i * MemoryManager.INODE_SIZE) != -1) {
			i++;
		}

        if (i == MemoryManager.MAX_INODES) {
            i = -1;
        }
        // Identifier le premier inode libre.
        // Retourner son numéro.

        return i;
    }

    public boolean createFile(
            String directory,
            String filename) {

        int inodeNum = allocateInode();

        if (inodeNum == -1) {
            return false;
        }

        // TODO:
        // Construire l'inode.
		Inode file = new Inode(memoryManager, inodeNum);
        // L'initialiser comme fichier vide.
		file.writeToMemory(1, 0, System.currentTimeMillis(), System.currentTimeMillis(), new int[10] , 0, (short) 0, 0);

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    public byte[] readFile(int inodeNum) {

        Inode inode =
                new Inode(memoryManager, inodeNum);

        int fileSize =
                inode.getFileSize();

        if (fileSize == 0) {
            return new byte[0];
        }

        byte[] fileData =
                new byte[fileSize];

        byte[] memory =
                memoryManager.getFilesystemMemory();

        int[] blockPointers =
                inode.getDirectPointers();

        // TODO:
        // Parcourir les blocs utilisés.
        // Copier chaque fragment vers fileData.
        for (int bloc = 0; bloc < 10; bloc++) { // Pas besoin de s'arrêter au premier pointeur nul comme il y a que 10 pointeur max
            for (int i = 0; i < MemoryManager.BLOCK_SIZE && bloc * MemoryManager.BLOCK_SIZE + i < fileSize; i ++) {
                fileData[bloc * MemoryManager.BLOCK_SIZE + i] = memory[blockPointers[bloc]  * MemoryManager.BLOCK_SIZE + i];
            }
        }

        return fileData;
    }

    public boolean writeFile(
            int inodeNum,
            byte[] data) {

        int blocksNeeded =
                (data.length
                + MemoryManager.BLOCK_SIZE - 1)
                / MemoryManager.BLOCK_SIZE;

        if (blocksNeeded > Inode.DIRECT_POINTERS) {
            return false;
        }



        int[] blockPointers =
                new int[Inode.DIRECT_POINTERS];

        // TODO:
        // Allouer blocksNeeded blocs.

        for (int i = 0; i < blocksNeeded; i++) {
            blockPointers[i] = memoryManager.allocateBlock();
        }

        byte[] memory =
                memoryManager.getFilesystemMemory();

        int bytesRemaining =
                data.length;

        int dataSrcOffset = 0;

        // TODO:
        // Pour chaque bloc :
        // - calculer la quantité à copier ;
        // - récupérer le numéro du bloc ;
        // - calculer son offset physique ;
        // - copier les données.

       
      


        for ( int bloc = 0; bloc < blocksNeeded; bloc++) {
            int aCopier =  Math.min(MemoryManager.BLOCK_SIZE, data.length - bloc * MemoryManager.BLOCK_SIZE);
            for (int i = 0; i < aCopier; i++) {
                memory[blockPointers[bloc]  * MemoryManager.BLOCK_SIZE + i] = data[bloc * MemoryManager.BLOCK_SIZE + i];
            }

        }

        



        // TODO:
        // Mettre à jour l'inode.
        Inode inode = new Inode(memoryManager, inodeNum);
        inode.updateInode(data.length, blockPointers);
        

        return true;
    }

    

}
