package uk.ac.standrews.cs.storage.implementations.aws.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.S3Object;
import uk.ac.standrews.cs.storage.data.Data;
import uk.ac.standrews.cs.storage.data.InputStreamData;
import uk.ac.standrews.cs.storage.exceptions.DataException;
import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.File;

import java.io.IOException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class AWSFile extends AWSStatefulObject implements File {

    public AWSFile(AmazonS3 s3Client, String bucketName, Directory parent,
                   String name) {
        super(s3Client, bucketName, parent, name);

        if (exists()) {
            retrieveData();
        }

    }

    public AWSFile(AmazonS3 s3Client, String bucketName, Directory parent,
                   String name, Data data) {
        super(s3Client, bucketName, parent, name);

        this.data = data;
    }

    @Override
    public boolean exists() {

        try (S3Object s3Object = s3Client.getObject(getObjectRequest)) {
            boolean objectExists = s3Object != null;
            // updateData(s3Object, objectExists); // FIXME - have this somewhere else?

            return objectExists;
        } catch (AmazonS3Exception e) {
            if (e.getStatusCode() == RESOURCE_NOT_FOUND) {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public String getPathname() {
        return logicalParent.getPathname() + name;
    }

    @Override
    public void setData(Data data) throws DataException {
        this.data = data;
    }

    @Override
    public Data getData() throws DataException {
        return data;
    }

    private void retrieveData() {
        try (S3Object s3Object = s3Client.getObject(getObjectRequest)) {
            boolean objectExists = s3Object != null;
            updateData(s3Object, objectExists);

        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateData(S3Object s3Object, boolean objectExists) {
        if (objectExists) {
            data = new InputStreamData(s3Object.getObjectContent());
        }
    }
}