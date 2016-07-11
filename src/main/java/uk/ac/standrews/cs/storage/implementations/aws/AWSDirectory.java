package uk.ac.standrews.cs.storage.implementations.aws;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import uk.ac.standrews.cs.storage.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.storage.implementations.NameObjectBindingImpl;
import uk.ac.standrews.cs.storage.interfaces.Directory;
import uk.ac.standrews.cs.storage.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.storage.interfaces.StatefulObject;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class represent an AWS directory.
 * AWS S3 does not have the concept of folders. Everything is a file.
 * To achieve consistent behaviour with the storage interfaces, we allow AWS directories
 * to be persisted by creating files with content-length zero.
 * E.g. on directory.persist(), we actually create the file "<DIR_NAME>/"
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class AWSDirectory extends AWSStatefulObject implements Directory {

    private static final String FOLDER_DELIMITER = "/";
    private static final String NO_DELIMITER = "";
    private static final int MINIMUM_NUMBER_OBJECT_FOR_FOLDER = 1;

    /**
     * Create an AWS folder (this is just an empty file finishing with the '/' delimiter)
     * @param s3Client
     * @param bucketName
     * @param parent
     * @param name
     * @param isImmutable
     */
    public AWSDirectory(AmazonS3 s3Client, String bucketName,
                        Directory parent, String name, boolean isImmutable) {
        super(s3Client, bucketName, parent, name, isImmutable);

        if (isImmutable && exists()) {
            this.persisted = true;
        } else {
            this.persisted = false;
        }
    }

    /**
     * Create a bucket for the given s3Client (this will be the root directory)
     * @param s3Client
     * @param bucketName
     */
    public AWSDirectory(AmazonS3 s3Client, String bucketName) {
        super(s3Client, bucketName);
    }

    @Override
    public String getPathname() {
        if (logicalParent == null) {
            return  "";
        } else if (name == null || name.isEmpty()) {
            return logicalParent.getPathname() + FOLDER_DELIMITER;
        } else {
            return logicalParent.getPathname() + name + FOLDER_DELIMITER;
        }
    }

    @Override
    public StatefulObject get(String name) throws BindingAbsentException {

        StatefulObject obj;
        if (isDirectory(name)) {
            name = name.substring(0, name.length() - 1);
            obj = new AWSDirectory(s3Client, bucketName, this, name, isImmutable);
        } else {
            obj = new AWSFile(s3Client, bucketName, this, name, isImmutable);
        }

        return obj;
    }

    /**
     * A folder exists if there is at least a file or another folder in it.
     *
     * TODO - check if possible to do it differently?
     * @return
     */
    @Override
    public boolean exists() {

        ListObjectsRequest objectsRequest = new ListObjectsRequest(bucketName,
                getPathname(),
                null,
                FOLDER_DELIMITER,
                MINIMUM_NUMBER_OBJECT_FOR_FOLDER);

        ObjectListing listing = s3Client.listObjects(objectsRequest);
        return thereIsAFile(listing) || thereIsAFolder(listing);
    }

    private boolean thereIsAFile(ObjectListing listing) {
        return !listing.getObjectSummaries().isEmpty();
    }

    private boolean thereIsAFolder(ObjectListing listing) {
        return !listing.getCommonPrefixes().isEmpty();
    }

    @Override
    public boolean contains(String name) {

        try {
            StatefulObject obj = get(name);
            return obj.exists();
        } catch (BindingAbsentException e) {
            return false;
        }
    }

    /**
     * Remove a file or a folder and its subfolders
     * @param name
     * @throws BindingAbsentException
     */
    @Override
    public void remove(String name) throws BindingAbsentException {
        if (isDirectory(name)) {
            removeFilesInDirectory(name);
        } else {
            deleteObject(name);
        }
    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {
        return new DirectoryIterator(false);
    }

    private void removeFilesInDirectory(String name) throws BindingAbsentException {

        Iterator<NameObjectBinding> iterator = new DirectoryIterator(name, true);
        while(iterator.hasNext()) {
            NameObjectBinding objectBinding = iterator.next();
            String objectToDeleteName = objectBinding.getName();
            
            if (!isDirectory(objectToDeleteName)) {
                remove(objectToDeleteName);
            } else {
                deleteObject(objectToDeleteName);
            }
        }
    }

    private void deleteObject(String objectToDeleteName) {
        s3Client.deleteObject(bucketName, getPathname() + objectToDeleteName);
    }

    private boolean isDirectory(String name) {
        return name.endsWith(FOLDER_DELIMITER);
    }

    private class DirectoryIterator implements Iterator<NameObjectBinding> {

        private static final int OBJECTS_PER_REQUESTS = 20;
        private static final int FIRST_FOLDER = 0;
        private static final String NO_PREFIX = "";

        private ObjectListing listing;
        private Iterator<S3ObjectSummary> summary;
        private List<String> folders = new LinkedList<>();
        private String prefix;
        private String delimiter;
        private boolean allLevels;

        /**
         *
         * @param prefix allows to narrow down the iteration on this folder
         * @param allLevels if true, return all files in the directory and subdirectories.
         *                  if false, return just the files in the directory and folders.
         *                  if false, skip this directory
         */
        public DirectoryIterator(String prefix, boolean allLevels) {
            this.prefix = prefix;
            this.allLevels = allLevels;

            if (allLevels) {
                delimiter = NO_DELIMITER;
            } else {
                delimiter = FOLDER_DELIMITER;
            }

            ListObjectsRequest objectsRequest = getListObjectRequest(null);
            initSummary(objectsRequest);
        }

        public DirectoryIterator(boolean allLevels) {
            this(NO_PREFIX, allLevels);
        }

        private void initSummary(ListObjectsRequest objectsRequest) {
            listing = s3Client.listObjects(objectsRequest);
            summary = listing.getObjectSummaries().iterator();
            folders.addAll(listing.getCommonPrefixes());
        }

        private ListObjectsRequest getListObjectRequest(String marker) {
            String path = getPathname() + prefix;
            ListObjectsRequest objectsRequest =
                    new ListObjectsRequest(bucketName,
                            path,
                            marker,
                            delimiter,
                            OBJECTS_PER_REQUESTS);

            return objectsRequest;
        }

        @Override
        public boolean hasNext() {
            skipThisFolder();

            boolean next = summary.hasNext() || !folders.isEmpty();

            if (!next) {
                String marker = listing.getNextMarker();

                if (marker == null) {
                    next = !folders.isEmpty();
                } else {
                    ListObjectsRequest objectsRequest = getListObjectRequest(marker);
                    initSummary(objectsRequest);
                    next = true;
                }
            }

            return next;
        }

        @Override
        public NameObjectBinding next() {
            skipThisFolder();

            if(!hasNext()) {
                return null;
            }

            try {
                String objectName;
                if (summary.hasNext()) {
                    S3ObjectSummary object = summary.next();
                    objectName = object.getKey();
                } else {
                    objectName = folders.remove(FIRST_FOLDER);
                }

                StatefulObject obj = get(objectName);
                return new NameObjectBindingImpl(objectName, obj);
            } catch (BindingAbsentException e) {
                e.printStackTrace();
            }

            return null;
        }


        private void skipThisFolder() {
            if (!allLevels && persisted && summary.hasNext()) {
                summary.next();
                allLevels = true;
            }
        }
    }
}
