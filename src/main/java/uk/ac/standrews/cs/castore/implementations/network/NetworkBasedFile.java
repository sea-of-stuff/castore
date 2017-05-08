package uk.ac.standrews.cs.castore.implementations.network;

import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.filesystem.FileBasedFile;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class NetworkBasedFile extends FileBasedFile implements IFile {

    public NetworkBasedFile(IDirectory parent, String name) throws StorageException {
        super(parent, name);
    }
}
