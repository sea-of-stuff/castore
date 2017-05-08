package uk.ac.standrews.cs.castore.utils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * IO Utility methods
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class IO {

    public static String InputStreamToString(InputStream string) throws IOException {
        return IOUtils.toString(string, StandardCharsets.UTF_8);
    }

    public static ByteArrayOutputStream InputStreamToByteArrayOutputStream(InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(input, baos);
        return baos;
    }
}
