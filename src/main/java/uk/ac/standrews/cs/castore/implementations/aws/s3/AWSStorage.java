package uk.ac.standrews.cs.castore.implementations.aws.s3;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.castore.exceptions.DestroyException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStorage;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.IFile;
import uk.ac.standrews.cs.castore.interfaces.IStorage;

import java.util.logging.Logger;

/**
 * AWSStorage abstracts the AWS S3 complexity. In doing so, it is possible to use
 * AWS as any normal data storage.
 *
 * TODO - check if files are cached
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class AWSStorage extends CommonStorage implements IStorage {

    private static Logger log = Logger.getLogger(AWSStorage.class.getName());

    private static final Region DEFAULT_REGION = Region.getRegion(Regions.EU_WEST_1);
    private static final int KEYS_PER_ITERATION = 20;

    private final Region region = DEFAULT_REGION;
    private final AmazonS3 s3Client;
    private String bucketName;

    /**
     * Crea an AWS S3 Storage using default credentials.
     *
     * From the AWS S3 SDK Documentation:
     * A credentials provider chain will be used that searches for credentials in
     * this order:
     * <ul>
     * <li>Environment Variables - AWS_ACCESS_KEY_ID and AWS_SECRET_KEY</li>
     * <li>Java System Properties - aws.accessKeyId and aws.secretKey</li>
     * <li>Credential profiles file at the default location (~/.aws/credentials) shared by all AWS SDKs and the AWS CLI</li>
     * <li>Instance Profile Credentials - delivered through the Amazon EC2
     * metadata service</li>
     * </ul>
     *
     * @param bucketName
     * @throws StorageException
     */
    public AWSStorage(String bucketName) throws StorageException {

        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region.toString())
                .withCredentials(new EnvironmentVariableCredentialsProvider())
                .build();

        createAndSetBucket(bucketName);
        createRoot();
    }

    public AWSStorage(final String accessKeyId, final String secretAccessKey, String bucketName) throws StorageException {

        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKeyId, secretAccessKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region.toString())
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();

        createAndSetBucket(bucketName);
        createRoot();
    }

    @Override
    public IDirectory createDirectory(IDirectory parent, String name) throws StorageException {
        return new AWSDirectory(s3Client, bucketName, parent, name);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename) throws StorageException {
        return new AWSFile(s3Client, bucketName, parent, filename);
    }

    @Override
    public IFile createFile(IDirectory parent, String filename, Data data) throws StorageException {
        return new AWSFile(s3Client, bucketName, parent, filename, data);
    }

    @Override
    public void destroy() throws DestroyException {

        try {
            final ListObjectsV2Request req = new ListObjectsV2Request()
                    .withBucketName(bucketName)
                    .withMaxKeys(KEYS_PER_ITERATION);

            ListObjectsV2Result result;
            do {
                result = s3Client.listObjectsV2(req);
                for (S3ObjectSummary objectSummary:result.getObjectSummaries()) {
                    root.remove(objectSummary.getKey());
                }

                String continuationToken = result.getNextContinuationToken();
                req.setContinuationToken(continuationToken);
            } while (result.isTruncated());

            s3Client.deleteBucket(bucketName);
        } catch (BindingAbsentException e) {
            throw new DestroyException(e);
        }

        root = null;
    }

    private void createAndSetBucket(String bucketName) throws StorageException {
        try {
            boolean bucketExist = s3Client.doesBucketExist(bucketName);
            if (!bucketExist) s3Client.createBucket(bucketName);


            this.bucketName = bucketName;

            while(true) {
                if (s3Client.doesBucketExist(bucketName)) break;

                log.info("Waiting for bucket creation");
                Thread.sleep(1000);
            }

        } catch (AmazonClientException | InterruptedException e) {
            throw new StorageException(e);
        }

    }

    private void createRoot() throws StorageException {
        try {
            root = new AWSDirectory(s3Client, bucketName);
            root.persist();
        } catch (PersistenceException e) {
            throw new StorageException(e);
        }
    }

}
