package uk.ac.standrews.cs.storage.implementations.network;

import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.filesystem.FileBasedFile;
import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.File;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class NetworkBasedFile extends FileBasedFile implements File {

    public NetworkBasedFile(Directory parent, String name) throws StorageException {
        super(parent, name);
    }
}
