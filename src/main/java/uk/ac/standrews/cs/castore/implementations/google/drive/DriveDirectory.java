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

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class DriveDirectory extends DriveStatefulObject implements IDirectory {

    private static final Logger log = Logger.getLogger(DriveDirectory.class.getName());

    private static final String DRIVE_FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";

    DriveDirectory(Drive drive, IDirectory parent, String name) throws StorageException {
        super(drive, parent, name);

        // Re-add trailing slash

        this.name = addTrailingSlash(name);
        this.objectPath = getPathname();
    }

    DriveDirectory(Drive drive, String name) throws StorageException {
        super(drive, name);

        // Re-add trailing slash
        this.name = addTrailingSlash(name);
        this.objectPath = getPathname();
    }

    @Override
    public String getPathname() {
        if (parent == null) {
            return name;
        } else if (name == null || name.isEmpty()) {
            return parent.getPathname();
        } else {
            return parent.getPathname() + name;
        }
    }

    /**
     *
     * @param name
     * @return
     * @throws BindingAbsentException
     */
    @Override
    public StatefulObject get(String name) throws BindingAbsentException {

        try {
            String id = getId(objectPath);
            List<File> list = drive.files()
                    .list()
                    .setQ("'" + id + "' in parents and name = '" + name + "'")
                    .setFields("files(id, name, mimeType)")
                    .execute()
                    .getFiles();

            if (!list.isEmpty()) {
                File found = list.get(0); // Ignore all other results

                switch(found.getMimeType()) {
                    case DRIVE_FOLDER_MIME_TYPE:
                        return new DriveDirectory(drive, this, name);
                    default:
                        return new DriveFile(drive, this, name);

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
            String id = getId(objectPath);
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
            String id = getId(objectPath);
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

        if (exists()) return;

        try {
            String parentId = null;
            if (parent != null) {

                // Make sure that the parent folder is persisted
                if (!parent.exists()) {
                    parent.persist();
                }
                parentId = getId(getParent().getPathname());
            }

            File fileMetadata = new File()
                    .setName(name)
                    .setMimeType(DRIVE_FOLDER_MIME_TYPE);

            if (parentId != null) fileMetadata.setParents(Collections.singletonList(parentId));

            drive.files()
                    .create(fileMetadata)
                    .setFields("id")
                    .execute();

        } catch (IOException e) {
            throw new PersistenceException("Unable to create folder with name " + name);
        }
    }

    @Override
    public Iterator<NameObjectBinding> getIterator() {

        try {
            String folderId = getId(objectPath);
            FileList list = drive.files()
                    .list()
                    .setQ("'" + folderId + "' in parents")
                    .setFields("nextPageToken, files(id, name)")
                    .execute();


            return new DirectoryIterator(list, folderId);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new DirectoryIterator();
    }

    private class DirectoryIterator implements Iterator<NameObjectBinding>  {

        private String folderId;
        private FileList list;
        private Iterator<File> files;

        DirectoryIterator(FileList list, String folderId) {
            this.list = list;
            files = list.getFiles().iterator();

            this.folderId = folderId;
        }

        DirectoryIterator() {
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
                                .setQ("'" + folderId + "' in parents")
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
