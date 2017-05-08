package uk.ac.standrews.cs.castore.implementations;

import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class CommonStorage implements IStorage {

    protected IDirectory root;

    @Override
    public IDirectory getRoot() {
        return root;
    }

}
