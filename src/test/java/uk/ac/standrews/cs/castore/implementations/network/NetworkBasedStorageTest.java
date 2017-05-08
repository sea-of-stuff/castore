package uk.ac.standrews.cs.castore.implementations.network;

import org.testng.annotations.Test;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;

import java.util.Iterator;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class NetworkBasedStorageTest {

    @Test (enabled = false)
    public void testGetRoot() throws Exception {

        NetworkBasedStorage storage = new NetworkBasedStorage("sic2", "public_html");

        IDirectory dir = storage.getRoot();
        Iterator it = dir.getIterator();
        while(it.hasNext()) {
            System.out.println(it.next().toString());
        }
    }

}