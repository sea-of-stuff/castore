package uk.ac.standrews.cs.castore.data;

import org.testng.annotations.Test;
import uk.ac.standrews.cs.castore.utils.IO;

import java.io.InputStream;

import static org.testng.Assert.assertEquals;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class InputStreamDataTest {

    @Test
    public void testWithSomeDataStream() {
        InputStream inputStream = IO.StringToInputStream("TEST");
        InputStreamData data = new InputStreamData(inputStream);
        assertEquals(4, data.getSize());
    }

    @Test
    public void testEmptyStream() {
        InputStream inputStream = IO.StringToInputStream("");
        InputStreamData data = new InputStreamData(inputStream);
        assertEquals(0, data.getSize());
    }

}
