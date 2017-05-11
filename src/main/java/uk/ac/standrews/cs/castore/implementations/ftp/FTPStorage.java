package uk.ac.standrews.cs.castore.implementations.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPReply;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.DestroyException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStorage;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class FTPStorage extends CommonStorage implements IStorage {

    private final static int KEEP_ALIVE_TIMEOUT = 300;

    private FTPClient ftp;

    public FTPStorage(String host) throws StorageException {

        ftp = new FTPClient();
        ftp.setControlKeepAliveTimeout(KEEP_ALIVE_TIMEOUT);

        FTPClientConfig config = new FTPClientConfig();
        ftp.configure(config);

        try {
            ftp.connect(host);
            System.out.println("Connected to " + host + ".");
            System.out.print(ftp.getReplyString());

            // After connection attempt, you should check the reply code to verify
            // success.
            int reply = ftp.getReplyCode();

            // TODO - move ?
            if(!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                throw new StorageException("Unable to connect to FTP host " + host);
            }


            // TODO - do something
            // ftp.retrieveFileStream()
            // ftp.storeFile()
            // ftp.deleteFile()

            ftp.logout(); // TODO - move, we want to keep it opened

        } catch(IOException e) {
            throw new StorageException(e);
        }
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        return null;
    }

    @Override
    public IDirectory createDirectory(String name) throws StorageException {
        return null;
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) throws StorageException {
        return null;
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) throws StorageException {
        return null;
    }

    @Override
    public void destroy() throws DestroyException {

        // FIXME - should not be here, but somewhere else

        if(ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch(IOException ioe) {
                // do nothing
            }
        }
    }
}
