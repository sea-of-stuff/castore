package uk.ac.standrews.cs.storage.implementations.network;

import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.filesystem.FileBasedDirectory;
import uk.ac.standrews.cs.storage.interfaces.Directory;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class NetworkBasedDirectory extends FileBasedDirectory {

    public NetworkBasedDirectory(Directory parent, String name, boolean isImmutable) throws StorageException {
        super(parent, name, isImmutable);
    }
}
