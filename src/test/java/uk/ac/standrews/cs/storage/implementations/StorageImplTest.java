package uk.ac.standrews.cs.storage.implementations;

import org.testng.annotations.Factory;
import org.testng.annotations.Test;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.data.StringData;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.IFile;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.util.Iterator;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class StorageImplTest extends StorageBaseTest {

    private static final Data TEST_DATA = new StringData("hello world");
    private final STORAGE_TYPE storageType;

    @Factory(dataProvider = "storage-manager-provider")
    public StorageImplTest(STORAGE_TYPE storageType) {
        this.storageType = storageType;
    }

    @Override
    protected STORAGE_TYPE getStorageType() {
        return storageType;
    }

    @Test
    public void createFile() throws StorageException {
        IFile file = storage.createFile(storage.getRoot(), "0-test.txt", TEST_DATA);
        file.persist();
        assertTrue(file.exists());
    }

    @Test
    public void checkFileInDirectory() throws StorageException {
        IFile file = storage.createFile(storage.getRoot(), "1-test.txt", TEST_DATA);
        file.persist();
        assertTrue(storage.getRoot().contains("1-test.txt"));
    }

    @Test
    public void checkFileInDirectoryFails() throws StorageException {
        IDirectory dirA = storage.createDirectory(storage.getRoot(), "2-folderA");
        dirA.persist();

        IDirectory dirB = storage.createDirectory(storage.getRoot(), "2-folderB");
        dirB.persist();

        IFile file = storage.createFile(dirA, "2-test.txt", TEST_DATA);
        file.persist();

        // Checking file in the wrong directory
        assertFalse(dirB.contains("2-test.txt"));
    }

    @Test
    public void fileInDirectoryDeletion() throws StorageException {
        IFile file = storage.createFile(storage.getRoot(), "3-test.txt", TEST_DATA);
        file.persist();
        assertTrue(storage.getRoot().contains("3-test.txt"));

        storage.getRoot().remove("3-test.txt");
        assertFalse(storage.getRoot().contains("3-test.txt"));
    }

    @Test
    public void getFileFromDirectory() throws StorageException {
        IFile file = storage.createFile(storage.getRoot(), "4-test.txt", TEST_DATA);
        file.persist();

        StatefulObject object = storage.getRoot().get("4-test.txt");
        assertTrue(object instanceof IFile);
    }

    @Test
    public void iteratorTest() throws StorageException {

        // Populate folder
        for(int i = 0 ; i < 15; i++) {
            storage.createFile(storage.getRoot(), "5-test-" + i + ".txt", TEST_DATA).persist();
        }

        int testCounter = 0;
        Iterator<NameObjectBinding> it = storage.getRoot().getIterator();
        while(it.hasNext()) {
            it.next().getName();
            testCounter++;
        }

        assertEquals(testCounter, 15); // Expecting logicalParent directory too
    }

    @Test
    public void iteratorInFolderTest() throws StorageException {

        IDirectory directory = storage.createDirectory(storage.getRoot(), "6-folder_with_files");

        // Populate folder
        for(int i = 0 ; i < 15; i++) {
            storage.createFile(directory, "6-test-" + i + ".txt", TEST_DATA).persist();
        }

        int testCounter = 0;
        Iterator<NameObjectBinding> it = directory.getIterator();
        while(it.hasNext()) {
            String name = it.next().getName();
            System.out.println(name);
            testCounter++;
        }

        assertEquals(testCounter, 15); // Expecting logicalParent directory too
    }

    @Test
    public void folderWithFileTest() throws StorageException, InterruptedException {
        IDirectory directory = storage.createDirectory(storage.getRoot(), "7-folder_with_file");
        storage.createFile(directory, "7-empty_file.txt").persist();

        boolean contains = storage.getRoot().contains("7-folder_with_file/");
        assertTrue(contains);
    }

    @Test
    public void emptyFolderTest() throws StorageException, InterruptedException {
        storage.createDirectory(storage.getRoot(), "8-empty_folder");

        boolean contains = storage.getRoot().contains("8-empty_folder/");
        assertFalse(contains);
    }

    @Test
    public void emptyFolderPersistedTest() throws StorageException, InterruptedException {
        storage.createDirectory(storage.getRoot(), "9-empty_folder").persist();

        boolean contains = storage.getRoot().contains("9-empty_folder/");
        assertTrue(contains);
    }

    @Test
    public void folderWithFolderTest() throws StorageException {
        IDirectory directory = storage.createDirectory(storage.getRoot(), "10-folder_with_folder");
        storage.createDirectory(directory, "10-inner_folder");

        boolean contains = storage.getRoot().contains("10-folder_with_folder/");
        assertFalse(contains);

        boolean containsInner = directory.contains("10-inner_folder/");
        assertFalse(containsInner);
    }

    @Test
    public void nestedFolderWithFileTest() throws StorageException {
        IDirectory directory = storage.createDirectory(storage.getRoot(), "11-folder_with_folder");
        IDirectory innerDirectory = storage.createDirectory(directory, "11-inner_folder");
        IFile file = storage.createFile(innerDirectory, "11-test.txt", TEST_DATA);
        file.persist();

        boolean contains = storage.getRoot().contains("11-folder_with_folder/");
        assertTrue(contains);

        boolean containsInner = directory.contains("11-inner_folder/");
        assertTrue(containsInner);
    }

    @Test
    public void nestedFolderPersistedTest() throws StorageException {
        IDirectory directory = storage.createDirectory(storage.getRoot(), "12-folder_with_folder");
        storage.createDirectory(directory, "12-inner_folder").persist();

        boolean contains = storage.getRoot().contains("12-folder_with_folder/");
        assertTrue(contains);

        boolean containsInner = directory.contains("12-inner_folder/");
        assertTrue(containsInner);
    }

    @Test
    public void getDataTest() throws StorageException {
        IFile file = storage.createFile(storage.getRoot(), "13-test.txt", TEST_DATA);
        file.persist();

        IFile retrievedFile = storage.createFile(storage.getRoot(), "13-test.txt");
        assertNotNull(retrievedFile.getData());
    }

    @Test
    public void directoryDeletion() throws StorageException {
        storage.createDirectory(storage.getRoot(), "14-folder").persist();
        assertTrue(storage.getRoot().contains("14-folder/"));

        storage.getRoot().remove("14-folder/");
        Iterator iterator = storage.getRoot().getIterator();
        assertFalse(iterator.hasNext());
    }
}
