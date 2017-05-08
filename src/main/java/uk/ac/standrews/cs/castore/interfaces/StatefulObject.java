package uk.ac.standrews.cs.castore.interfaces;

import uk.ac.standrews.cs.castore.exceptions.PersistenceException;

import java.io.File;
import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface StatefulObject {

    IDirectory getLogicalParent();

    boolean exists();

    String getName();

    String getPathname();

    long lastModified();

    // TODO - is this method necessary?
    File toFile() throws IOException;

    void persist() throws PersistenceException;

    long getSize();

}
