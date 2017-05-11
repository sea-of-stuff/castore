package uk.ac.standrews.cs.castore.implementations.google.drive;


import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * The index keeps track of the object identifiers
 *
 * TODO - find solution that does not rely on this
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
class Index implements Serializable {

    // Map path --> object-id, type
    // Map should be persisted to disk
    private HashMap<String, String[]> index;

    public static final String DIRECTORY_TYPE = "dir";
    public static final String FILE_TYPE = "file";

    Index() {
        index = new HashMap<>();
    }

    String getObjectId(String path) {
        if (!index.containsKey(path)) return null;

        return index.get(path)[0];
    }

    String getObjectType(String path) {
        if (!index.containsKey(path)) return null;

        return index.get(path)[1];
    }

    void remove(String path) {
        index.remove(path);
    }

    void setPathId(String path, String objectId, String type) {
        index.put(path, new String[]{objectId, type});
    }

    void empty() {
        index.clear();
    }

    Iterator<String> getAllIds() {
        return index.entrySet()
                .stream()
                .map(e -> e.getValue()[0])
                .collect(Collectors.toList())
                .iterator();
    }

    //////////////
    // FILE OPS //
    //////////////

    void persist(String path) throws IOException {

        File file = new File(path);
        try (FileOutputStream ostream = new FileOutputStream(file);
            ObjectOutputStream p = new ObjectOutputStream(ostream)) {

            p.writeObject(this);
            p.flush();
        }
    }

    void delete(String path) {
        File file = new File(path);
        file.delete();
    }

    static Index load(String path) throws IOException, ClassNotFoundException {

        File file = new File(path);
        if (!file.exists()) throw new IOException("Cannot load index");

        // Check that file is not empty
        try (BufferedReader br = new BufferedReader(new FileReader(file.getPath()))) {
            if (br.readLine() == null) {
                return null;
            }
        }

        try (FileInputStream istream = new FileInputStream(file)) {
            ObjectInputStream q = new ObjectInputStream(istream);

            return (Index) q.readObject();
        }
    }
}
