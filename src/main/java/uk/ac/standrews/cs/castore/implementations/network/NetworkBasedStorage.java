package uk.ac.standrews.cs.castore.implementations.network;

import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DestroyException;
import uk.ac.standrews.cs.castore.implementations.CommonStorage;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class NetworkBasedStorage extends CommonStorage implements IStorage {

    private static final String VOLUMES_PATH = "/Volumes/";
    private String mountPoint;
    private String rootPath;

    private IDirectory root;

    /**
     * Create the IStorage for a mounted network folder.
     * The storage should be mounted under /Volumes
     *
     * @param mountPoint
     * @param rootPath
     */
    public NetworkBasedStorage(String mountPoint, String rootPath) {

        if (mountPoint != null && !mountPoint.isEmpty() &&
                rootPath != null && !rootPath.isEmpty()) {
            this.mountPoint = mountPoint;
            this.rootPath = rootPath;

//            root = new NetworkBasedDirectory(VOLUMES_PATH +
//                    mountPoint + "/" + rootPath);
        }
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) {
        return null;
    }

    @Override
    public IDirectory createDirectory(String name) {
        return null;
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) {
        return null;
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) {
        return null;
    }

    @Override
    public void destroy() throws DestroyException {

    }
}
