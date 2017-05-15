package uk.ac.standrews.cs.castore.implementations.google.drive;

import com.google.api.services.drive.DriveRequest;

import java.io.IOException;

/**
 * Wraps Drive Commands, so that we can enforce a retry policy on failure
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DriveWrapper<T> {

    // TODO - keep track of global number of retries

    public static <T> T Execute(DriveRequest<T> request) throws DriveException {

        int retry = 3;
        while(retry > 0) {
            try {
                return request.execute();

            } catch (IOException e) {
                retry--;

                try {
                    Thread.sleep(2000); // Cool down for 2 seconds
                } catch (InterruptedException e1) {
                    throw new DriveException();
                }
            }
        }

        throw new DriveException();
    }
}
