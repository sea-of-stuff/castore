package uk.ac.standrews.cs.castore.implementations.network;

import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.filesystem.FileBasedDirectory;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class NetworkBasedDirectory extends FileBasedDirectory {

    public NetworkBasedDirectory(IDirectory parent, String name) throws StorageException {
        super(parent, name);
    }
}
