package uk.ac.standrews.cs.castore.implementations.google.drive;

import com.google.api.services.drive.Drive;
import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.StorageException;
import uk.ac.standrews.cs.castore.implementations.CommonStatefulObject;
import uk.ac.standrews.cs.castore.interfaces.IDirectory;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static uk.ac.standrews.cs.castore.CastoreConstants.FOLDER_DELIMITER;
import static uk.ac.standrews.cs.castore.CastoreConstants.FOLDER_DELIMITER_CHAR;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class DriveStatefulObject extends CommonStatefulObject implements StatefulObject {

    protected Drive drive;

    protected IDirectory parent;
    protected String objectPath;
    protected Data data;

    DriveStatefulObject(Drive drive, IDirectory parent, String name) throws StorageException  {
        super(name);

        this.drive = drive;
        this.parent = parent;
        this.objectPath = getPathname();
    }

    DriveStatefulObject(Drive drive, String name) throws StorageException {
        super(name);

        this.drive = drive;
        this.objectPath = getPathname();
    }

    @Override
    public IDirectory getParent() {
        return parent;
    }

    @Override
    public boolean exists() {

        try {
            return getFile() != null;
        } catch (IOException e) {
            return false;
        }

    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long lastModified() {

        try {
            return getFile().getModifiedTime().getValue();
        } catch (IOException e) {
            return 0;
        }
    }

    @Override
    public File toFile() throws IOException {
        return null;
    }

    @Override
    public long getSize() {

        try {
            com.google.api.services.drive.model.File file = getFile();
            if (file != null) {
                return file.getSize();
            }
        } catch (IOException e) {
            return 0;
        }

        return 0;
    }

    protected String getId() {
        return getId(objectPath);
    }

    protected String getId(String path) {

        String[] pathComponents = path.split(FOLDER_DELIMITER);
        if (path.charAt(path.length() - 1) == FOLDER_DELIMITER_CHAR) {
            pathComponents[pathComponents.length - 1] += FOLDER_DELIMITER_CHAR;
        }

        String parentId = null;
        for(String component:pathComponents) {

            try {
                String query = "name = '" + component + "'";
                if (parentId != null) {
                    query += " and '" + parentId + "' in parents";
                }

                List<com.google.api.services.drive.model.File> list = drive.files()
                        .list()
                        .setQ(query)
                        .setFields("files(id, name)")
                        .execute()
                        .getFiles();

                if (!list.isEmpty()) {
                    parentId = list.get(0).getId();
                }

            } catch (IOException e) {
                return null;
            }
        }

        return parentId;
    }

    /**
     *
     * @return the id of the file. Null if the file is unknown
     * @throws IOException
     */
    protected com.google.api.services.drive.model.File getFile() throws IOException {

        String id = getId();
        if (id == null) return null;

        return getFile(id);
    }

    // See fields - https://developers.google.com/drive/v3/reference/files#resource
    protected com.google.api.services.drive.model.File getFile(String id) throws IOException {

        return drive.files()
                .get(id)
                .setFields("id, kind, name, modifiedTime, size")
                .execute();
    }

}
