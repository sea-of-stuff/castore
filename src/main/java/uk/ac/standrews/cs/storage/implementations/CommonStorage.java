package uk.ac.standrews.cs.storage.implementations;

import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class CommonStorage implements IStorage {

    protected Directory root;
    protected boolean isImmutable;

    public CommonStorage(boolean isImmutable) {
        this.isImmutable = isImmutable;
    }

    @Override
    public boolean isImmutable() {
        return isImmutable;
    }

    @Override
    public Directory getRoot() {
        return root;
    }

}
