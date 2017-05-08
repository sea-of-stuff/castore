package uk.ac.standrews.cs.castore.data;

import org.testng.annotations.Test;
import uk.ac.standrews.cs.castore.HelperTest;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static org.testng.Assert.assertEquals;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class InputStreamDataTest {

    @Test
    public void testWithSomeDataStream() throws UnsupportedEncodingException {
        InputStream inputStream = HelperTest.StringToInputStream("TEST");
        InputStreamData data = new InputStreamData(inputStream);
        assertEquals(4, data.getSize());
    }

    @Test
    public void testEmptyStream() throws UnsupportedEncodingException {
        InputStream inputStream = HelperTest.StringToInputStream("");
        InputStreamData data = new InputStreamData(inputStream);
        assertEquals(0, data.getSize());
    }

    @Test
    public void testNullStream() throws IOException {

//        InputStream inputStream = mock(InputStream.class);
//        when(inputStream.read()).thenReturn(-1);
//        when(inputStream.available()).thenReturn(0);
//
//        InputStreamData data = new InputStreamData(inputStream);
//        assertEquals(0, data.getSize());
    }

}