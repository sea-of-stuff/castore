package uk.ac.standrews.cs.castore.implementations.filesystem;

import org.testng.annotations.Test;
import uk.ac.standrews.cs.castore.CastoreBuilder;
import uk.ac.standrews.cs.castore.CastoreFactory;
import uk.ac.standrews.cs.castore.CastoreType;
import uk.ac.standrews.cs.castore.data.ByteData;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.util.Random;


/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FileBasedFileTest {

    @Test (enabled = false)
    public void performanceTest() throws StorageException {

        CastoreBuilder castoreBuilder = new CastoreBuilder().setType(CastoreType.LOCAL).setRoot("/tmp/storage/");
        IStorage storage = CastoreFactory.createStorage(castoreBuilder);


        byte[] b = new byte[1024*1024]; // 1mb
        new Random().nextBytes(b);

        long start = System.nanoTime();
        for(int i = 0; i < 10; i++) {
            IFile file = storage.createFile("test" + i, new ByteData(b));
            file.persist();
        }
        System.out.println("Time to add data " + (System.nanoTime() - start) / 1000000000.0 + " seconds");
    }
}