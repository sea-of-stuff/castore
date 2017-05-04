package uk.ac.standrews.cs.storage.implementations;

import uk.ac.standrews.cs.storage.exceptions.StorageException;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class CommonStatefulObject {

    private static final String ILLEGAL_CHARS_MAC = ":";
    private static final String ILLEGAL_CHARS_LINUX = "/\0";
    private static final String ILLEGAL_CHARS_WINDOWS = "<>:\"/\\|?\\*";

    private static final String ILLEGAL_CHARS = ILLEGAL_CHARS_MAC + ILLEGAL_CHARS_LINUX + ILLEGAL_CHARS_WINDOWS;
    private static final String LEGAL_CHARS_PATTERN = "[^" + ILLEGAL_CHARS + "]*";

    protected String name;

    public CommonStatefulObject(String name) throws StorageException {
        this.name = name;

        if (!NameIsLegal(name)) throw new StorageException();
    }

    public CommonStatefulObject() {}

    public abstract String getPathname();

    @Override
    public String toString() {
        return getPathname();
    }

    /**
     * Check that the repository name is legal.
     * A name is legal if:
     * - it exists and it has at least one character
     * - it is a valid file name for the file system
     *
     * TODO - consider limiting the size of the name to 31 characters for better compatability with old file systems?
     * @param name to be checked
     * @return true if the name is legal
     */
    public static boolean NameIsLegal(String name) {

        return name != null && !name.isEmpty() && name.matches(LEGAL_CHARS_PATTERN);
    }

}
