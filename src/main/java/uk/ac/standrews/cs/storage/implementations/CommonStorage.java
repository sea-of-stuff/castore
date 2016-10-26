package uk.ac.standrews.cs.storage.implementations;

import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class CommonStorage implements IStorage {

    protected Directory root;

    @Override
    public Directory getRoot() {
        return root;
    }

}
