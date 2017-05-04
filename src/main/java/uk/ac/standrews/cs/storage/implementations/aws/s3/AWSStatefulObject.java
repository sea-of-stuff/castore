package uk.ac.standrews.cs.storage.implementations.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.NullInputStream;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.exceptions.PersistenceException;
import uk.ac.standrews.cs.storage.exceptions.StorageException;
import uk.ac.standrews.cs.storage.implementations.CommonStatefulObject;
import uk.ac.standrews.cs.storage.interfaces.IDirectory;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class AWSStatefulObject extends CommonStatefulObject implements StatefulObject {

    private static final Logger log = Logger.getLogger(AWSStatefulObject.class.getName());

    protected static final int RESOURCE_NOT_FOUND = 404;
    private static final String TMP_FILE_PREFIX = "aws";
    private static final String TMP_FILE_SUFFIX = ".tmp";

    protected AmazonS3 s3Client;
    protected String bucketName;
    protected IDirectory logicalParent;
    protected Data data;
    protected GetObjectRequest getObjectRequest;

    public AWSStatefulObject(AmazonS3 s3Client, String bucketName, IDirectory parent, String name) throws StorageException {
        super(name);

        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.logicalParent = parent;

        String objectPath = getPathname();
        getObjectRequest = new GetObjectRequest(bucketName, objectPath);
    }

    public AWSStatefulObject(AmazonS3 s3Client, String bucketName) {

        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public IDirectory getLogicalParent() {
        return logicalParent;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long lastModified() {

        try (S3Object s3Object = s3Client.getObject(getObjectRequest)) {
            ObjectMetadata metadata = s3Object.getObjectMetadata();

            Date date = metadata.getLastModified();
            return date.getTime();
        } catch (IOException e) {
            log.log(Level.WARNING, "Unable to get the last modified date for the AWS Object", e);
        }

       return 0;
    }

    @Override
    public File toFile() throws IOException {

        try {
            final File tempFile = File.createTempFile(TMP_FILE_PREFIX, TMP_FILE_SUFFIX);
            tempFile.deleteOnExit();

            try (FileOutputStream output = new FileOutputStream(tempFile);
                 InputStream input = s3Client.getObject(getObjectRequest).getObjectContent()) {

                IOUtils.copy(input, output);
            }

            return tempFile;
        } catch (AmazonS3Exception e) {
            throw new IOException(e);
        }
    }

    @Override
    public void persist() throws PersistenceException {

        try (InputStream inputStream = getInputStream()) {

            String objectPath = getPathname();
            ObjectMetadata metadata = getObjectMetadata();

            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectPath, inputStream, metadata);
            s3Client.putObject(putObjectRequest);

        } catch (IOException e) {
            log.log(Level.SEVERE, "Unable to persiste the AWS Object", e);
        }
    }

    private InputStream getInputStream() throws IOException {
        return data != null ? data.getInputStream() : new NullInputStream(0);
    }

    private ObjectMetadata getObjectMetadata() {
        long contentLength = data != null ? data.getSize() : 0;
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(contentLength);

        return metadata;
    }
}
