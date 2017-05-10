package uk.ac.standrews.cs.castore.implementations.google.drive;


import java.io.*;
import java.util.HashMap;

/**
 * The index keeps track of the object identifiers
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
class Index implements Serializable {

    // Map path --> object-id
    // Map should be persisted to disk
    private HashMap<String, String> index;

    Index() {
        index = new HashMap<>();
    }

    String getObjectId(String path) {
        return index.get(path);
    }

    void setPathId(String path, String objectId) {
        index.put(path, objectId);
    }

    void empty() {
        index.clear();
    }

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

    static Index load(File file) throws IOException, ClassNotFoundException {

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
