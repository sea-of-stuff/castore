package uk.ac.standrews.cs.castore.implementations;

import uk.ac.standrews.cs.castore.data.Data;
import uk.ac.standrews.cs.castore.exceptions.RenameException;
import uk.ac.standrews.cs.castore.exceptions.StorageException;

import static uk.ac.standrews.cs.castore.CastoreConstants.FOLDER_DELIMITER_CHAR;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class CommonStatefulObject {

    private static final String ILLEGAL_CHARS_MAC = ":";
    private static final String ILLEGAL_CHARS_LINUX = "\0"; // Slash is accepted
    private static final String ILLEGAL_CHARS_WINDOWS = "<>:\"\\|?\\*"; // Slash is accepted

    private static final String ILLEGAL_CHARS = ILLEGAL_CHARS_MAC + ILLEGAL_CHARS_LINUX + ILLEGAL_CHARS_WINDOWS;
    private static final String LEGAL_CHARS_PATTERN = "[^" + ILLEGAL_CHARS + "]*";

    protected String name;
    protected Data data;

    public CommonStatefulObject(String name) throws StorageException {

        if (!NameIsLegal(name)) throw new StorageException();

        this.name = normalise(name);
    }

    public CommonStatefulObject(String name, Data data) throws StorageException {
        this(name);

        this.data = data;
    }

    protected CommonStatefulObject() {}

    public abstract String getPathname();

    public void rename(String newName) throws RenameException {}

    @Override
    public String toString() {
        return getPathname();
    }

    /**
     * Remove trailing slash
     *
     * @param path
     * @return
     */
    protected String normalise(String path) {
        if (path.charAt(path.length() - 1) == FOLDER_DELIMITER_CHAR) {
            path = path.substring(0, path.length() - 1);
        }

        return path;
    }

    protected String addTrailingSlash(String path) {
        if (path.charAt(path.length() - 1) != FOLDER_DELIMITER_CHAR) {
            path += FOLDER_DELIMITER_CHAR;
        }

        return path;
    }

    /**
     * Check that the repository name is legal.
     * A name is legal if:
     * - it exists and it has at least one character
     * - it is a valid file name for the file system
     *
     * TODO - consider limiting the size of the name to 31 characters for better compatability with old file systems?
     *
     * @param name to be checked
     * @return true if the name is legal
     */
    static boolean NameIsLegal(String name) {

        return name != null && !name.isEmpty() && name.matches(LEGAL_CHARS_PATTERN);
    }

}
