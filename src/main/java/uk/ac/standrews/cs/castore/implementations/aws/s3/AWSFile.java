package uk.ac.standrews.cs.castore.implementations.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.data.InputStreamData;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class AWSFile extends AWSStatefulObject implements IFile {

    private static final Logger log = Logger.getLogger(AWSFile.class.getName());

    public AWSFile(AmazonS3 s3Client, String bucketName, IDirectory parent, String name) throws StorageException {
        super(s3Client, bucketName, parent, name);

        if (exists()) retrieveAndUpdateData();

    }

    public AWSFile(AmazonS3 s3Client, String bucketName, IDirectory parent, String name, Data data) throws StorageException {
        super(s3Client, bucketName, parent, name);

        this.data = data;
    }

    @Override
    public boolean exists() {
        boolean objectExists = false;

        try (S3Object s3Object = s3Client.getObject(getObjectRequest)) {

            objectExists = s3Object != null;

        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == RESOURCE_NOT_FOUND) {
                objectExists = false;
            }
        } catch (IOException e) {
            log.log(Level.WARNING, "Unable to determine if the AWS object exists", e);
        }

        return objectExists;
    }

    @Override
    public String getPathname() {
        return parent.getPathname() + name;
    }

    @Override
    public long getSize() {
        try (S3Object s3Object = s3Client.getObject(getObjectRequest)) {

            return s3Object.getObjectMetadata().getContentLength();

        } catch (IOException e) {
            log.log(Level.WARNING, "Unable to get the size of of the AWS File", e);
        }

        return 0;
    }

    @Override
    public void setData(Data data) {
        this.data = data;
    }

    @Override
    public Data getData() {
        return data;
    }

    private void retrieveAndUpdateData() {
        try (S3Object s3Object = s3Client.getObject(getObjectRequest)) {

            boolean objectExists = s3Object != null;
            updateData(s3Object, objectExists);

        } catch (AmazonS3Exception | IOException e) {
            log.log(Level.SEVERE, "Unable to retrieve and/or update the data for the AWS File", e);
        }
    }

    private void updateData(S3Object s3Object, boolean objectExists) {
        if (objectExists) {
            data = new InputStreamData(s3Object.getObjectContent());
        }
    }
}
