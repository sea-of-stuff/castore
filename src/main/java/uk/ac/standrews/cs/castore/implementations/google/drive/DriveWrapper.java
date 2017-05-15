package uk.ac.standrews.cs.castore.implementations.google.drive;

import com.google.api.services.drive.DriveRequest;

import java.io.IOException;

/**
 * Wraps Drive Commands, so that we can enforce a retry policy on failure
 *
 * CircuitBreaker-like pattern used
 * @see "https://martinfowler.com/bliki/CircuitBreaker.html"
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
class DriveWrapper<T> {

    private static final int TOTAL_CONSECUTIVE_RETRIES = 10;
    private static int totalRetries = TOTAL_CONSECUTIVE_RETRIES;

    private static final int COOL_DOWN_TIMER = 2000;

    static <T> T Execute(DriveRequest<T> request) throws DriveException {

        int retry = 3;
        while(retry > 0 && totalRetries > 0) {
            try {

                T t = request.execute();
                totalRetries = TOTAL_CONSECUTIVE_RETRIES; // Resetting the global counter
                return t;

            } catch (IOException e) {
                retry--;
                totalRetries--;

                try {
                    Thread.sleep(COOL_DOWN_TIMER);
                } catch (InterruptedException e1) {
                    throw new DriveException();
                }
            }
        }

        throw new DriveException();
    }
}
