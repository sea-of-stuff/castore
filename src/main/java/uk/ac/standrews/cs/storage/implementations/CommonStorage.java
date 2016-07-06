package uk.ac.standrews.cs.storage.implementations;

import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.IStorage;

import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class CommonStorage implements IStorage {

    public static final String DATA_DIRECTORY_NAME = "data";
    public static final String MANIFESTS_DIRECTORY_NAME = "manifests";
    public static final String TEST_DATA_DIRECTORY_NAME = "test_data";

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

    @Override
    public Directory getDataDirectory() throws IOException {
        return createDirectory(DATA_DIRECTORY_NAME);
    }

    @Override
    public Directory getManifestDirectory() throws IOException {
        return createDirectory(MANIFESTS_DIRECTORY_NAME);
    }

    @Override
    public Directory getTestDirectory() throws IOException {
        return createDirectory(TEST_DATA_DIRECTORY_NAME);
    }

    protected void createSOSDirectories() throws PersistenceException, IOException {
        createDirectory(DATA_DIRECTORY_NAME).persist();
        createDirectory(MANIFESTS_DIRECTORY_NAME).persist();
        createDirectory(TEST_DATA_DIRECTORY_NAME).persist();
    }
}
