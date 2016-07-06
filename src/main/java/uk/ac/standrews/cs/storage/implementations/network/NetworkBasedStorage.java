package uk.ac.standrews.cs.storage.implementations.network;

import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.DestroyException;
import uk.ac.standrews.cs.storage.implementations.CommonStorage;
import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.File;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class NetworkBasedStorage extends CommonStorage implements IStorage {

    private static final String VOLUMES_PATH = "/Volumes/";
    private String mountPoint;
    private String rootPath;

    private Directory root;

    /**
     * Create the IStorage for a mounted network folder.
     * The storage should be mounted under /Volumes
     *
     * @param mountPoint
     * @param rootPath
     */
    public NetworkBasedStorage(String mountPoint, String rootPath, boolean isImmutable) {
        super(isImmutable);

        if (mountPoint != null && !mountPoint.isEmpty() &&
                rootPath != null && !rootPath.isEmpty()) {
            this.mountPoint = mountPoint;
            this.rootPath = rootPath;

//            root = new NetworkBasedDirectory(VOLUMES_PATH +
//                    mountPoint + "/" + rootPath);
        }
    }

    @Override
    public Directory createDirectory(Directory parent, String name) {
        return null;
    }

    @Override
    public Directory createDirectory(String name) {
        return null;
    }

    @Override
    public File createFile(Directory parent, String filename) {
        return null;
    }

    @Override
    public File createFile(Directory parent, String filename, Data data) {
        return null;
    }

    @Override
    public void destroy() throws DestroyException {

    }
}
