package uk.ac.standrews.cs.storage.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class Time {

    public static long DateToLong(String date) throws ParseException {

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        f.setTimeZone(TimeZone.getTimeZone("GMT"));

        Date d = f.parse(date);
        return d.getTime();
    }
}
