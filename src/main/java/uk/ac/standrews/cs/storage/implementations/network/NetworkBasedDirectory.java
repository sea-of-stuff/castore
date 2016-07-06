package uk.ac.standrews.cs.storage.implementations.network;

import uk.ac.standrews.cs.storage.implementations.filesystem.FileBasedDirectory;
import uk.ac.standrews.cs.storage.interfaces.Directory;

import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class NetworkBasedDirectory extends FileBasedDirectory {

    public NetworkBasedDirectory(Directory parent, String name, boolean isImmutable) throws IOException {
        super(parent, name, isImmutable);
    }
}
