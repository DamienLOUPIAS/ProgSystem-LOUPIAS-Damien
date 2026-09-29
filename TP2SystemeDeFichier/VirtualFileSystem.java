import java.util.*;

public class VirtualFileSystem {

    private MemoryManager memoryManager;

    public VirtualFileSystem() {
        this.memoryManager =
                new MemoryManager();
    }
	
	private void initialisation() {
		
	}

    private int allocateInode() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        // Parcourir les inodes de 0 à MAX_INODES - 1.
		int i = 0;
		while (i < MemoryManager.INODE_SIZE && Utils.readInt(memory, MemoryManager.INODE_TABLE_OFFSET + i * MemoryManager.INODE_SIZE) != 0) {
			i++;
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
		file.writeToMemory(0, 0, System.currentTimeMillis(), System.currentTimeMillis(), new int[10] , 0, (short) 0, 0);

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }
}
