package uk.ac.standrews.cs.storage.implementations.network;

import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.filesystem.FileBasedFile;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class NetworkBasedFile extends FileBasedFile implements IFile {

    public NetworkBasedFile(IDirectory parent, String name) throws StorageException {
        super(parent, name);
    }
}
