package uk.ac.standrews.cs.castore.implementations.google.drive;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import uk.ac.standrews.cs.castore.exceptions.BindingAbsentException;
import uk.ac.standrews.cs.castore.exceptions.PersistenceException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.NameObjectBindingImpl;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static uk.ac.standrews.cs.castore.CastoreConstants.FOLDER_DELIMITER;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DriveDirectory extends DriveStatefulObject implements IDirectory {

    private static final Logger log = Logger.getLogger(DriveDirectory.class.getName());

    private static final String DRIVE_FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";

    private String id;

    DriveDirectory(Drive drive, Index index, IDirectory parent, String name) throws StorageException {
        super(drive, index, parent, name);

        // Re-add trailing slash
        this.name = name + "/";
    }

    DriveDirectory(Drive drive, Index index, String name) throws StorageException {
        super(drive, index, name);

        // Re-add trailing slash
        this.name = name + "/";
    }

    @Override
    public String getPathname() {
        if (logicalParent == null) {
            return name + FOLDER_DELIMITER;
        } else if (name == null || name.isEmpty()) {
            return logicalParent.getPathname() + FOLDER_DELIMITER;
        } else {
            return logicalParent.getPathname() + name + FOLDER_DELIMITER;
        }
    }

    /**
     * FIXME - Will not work if the directory is not persisted
     *
     * @param name
     * @return
     * @throws BindingAbsentException
     */
    @Override
    public StatefulObject get(String name) throws BindingAbsentException {

        try {
            String id = index.getObjectId(objectPath); // TODO - Store this id here?
            List<File> list = drive.files()
                    .list()
                    .setQ("'" + id + "' in parents and name = '" + name + "'")
                    .setFields("files(id, name, kind, mimeType)")
                    .execute()
                    .getFiles();

            if (!list.isEmpty()) {
                File found = list.get(0); // Ignore all other results

                switch(found.getMimeType()) {
                    case DRIVE_FOLDER_MIME_TYPE:
                        // TODO - pass id around
                        return new DriveDirectory(drive, index, this, name);
                    default:
                        return new DriveFile(drive, index, this, name);

                }
            }

            throw new BindingAbsentException("Object does not exist");

        } catch (IOException| StorageException e) {
            throw new BindingAbsentException("Unable to return request object");
        }

    }

    @Override
    public boolean contains(String name) {

        try {
            String id = index.getObjectId(objectPath); // TODO - Store this id here?
            List<File> list = drive.files()
                    .list()
                    .setQ("'" + id + "' in parents and name = '" + name + "'")
                    .setFields("files(id, name)")
                    .execute()
                    .getFiles();

            return !list.isEmpty();

        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public void remove(String name) throws BindingAbsentException {

        try {
            String id = index.getObjectId(objectPath); // TODO - Store this id here?
            List<File> list = drive.files()
                    .list()
                    .setQ("'" + id + "' in parents and name = '" + name + "'")
                    .setFields("files(id, name)")
                    .execute()
                    .getFiles();

            if (!list.isEmpty()) {
                File found = list.get(0); // Ignore all other results
                drive.files().delete(found.getId()).execute();
            }

        } catch (IOException e) {
            throw new BindingAbsentException("Unable to delete object");
        }
    }

    @Override
    public void persist() throws PersistenceException {

        try {
            String parentId = null;
            if (logicalParent != null) {
                parentId = index.getObjectId(getParent().getPathname());
            }

            File fileMetadata = new File()
                    .setName(name)
                    .setMimeType(DRIVE_FOLDER_MIME_TYPE);

            if (parentId != null) fileMetadata.setParents(Collections.singletonList(parentId));

            System.out.println("Persist - Path " + getPathname() + " Name " + name);
            System.out.println("Persist - Parent " + parentId);

            File file = drive.files()
                    .create(fileMetadata)
                    .setFields("id")
                    .execute();

            index.setPathId(objectPath, file.getId(), Index.DIRECTORY_TYPE);
        } catch (IOException e) {
            throw new PersistenceException("Unable to create folder with name " + name);
        }
    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {

        try {
            String folderId = index.getObjectId(objectPath);
            FileList list = drive.files()
                    .list()
                    .setQ("'" + folderId + "' in parents")
                    .setFields("nextPageToken, files(id, name)")
                    .execute();


            return new DirectoryIterator(list);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new DirectoryIterator();
    }

    private class DirectoryIterator implements Iterator<NameObjectBinding>  {

        private FileList list;
        private Iterator<File> files;

        public DirectoryIterator(FileList list) {
            this.list = list;
            files = list.getFiles().iterator();
        }

        public DirectoryIterator() {
            files = Collections.EMPTY_LIST.iterator();
        }

        @Override
        public boolean hasNext() {
            boolean hasNext = files.hasNext();

            if (!hasNext && list != null) {

                hasNext = list.getNextPageToken() != null;
                if (hasNext) {
                    String nextToken = list.getNextPageToken();

                    try {
                        list = drive.files()
                                .list()
                                .setQ("'" + id + "' in parents")
                                .setFields("nextPageToken, files(id, name)")
                                .setPageToken(nextToken)
                                .execute();

                        files = list.getFiles().iterator();
                    } catch (IOException e) {
                        return false;
                    }
                }
            }

            return hasNext;
        }

        @Override
        public NameObjectBinding next() {

            if (!hasNext()) {
                return null;
            }

            try {
                File file = files.next();
                String name = file.getName();
                StatefulObject obj = get(name); // TODO - what if it is a folder?

                return new NameObjectBindingImpl(name, obj);
            } catch (BindingAbsentException e) {
                log.log(Level.SEVERE, "Unable to create a Binding Object for the next element from the iterator");
            }

            return null;
        }
    }
}
