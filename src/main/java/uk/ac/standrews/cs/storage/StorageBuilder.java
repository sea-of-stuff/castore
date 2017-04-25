package uk.ac.standrews.cs.storage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class StorageBuilder {

    public static final int NOT_SET = -1;

    private StorageType type;
    private String root;
    private String mountPoint;
    private String hostname;
    private int port = NOT_SET;

    public StorageBuilder setType(StorageType type) {
        this.type = type;
        return this;
    }

    public StorageBuilder setRoot(String root) {
        this.root = root;
        return this;
    }

    public StorageBuilder setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
        return this;
    }

    public StorageBuilder setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public StorageBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public StorageType getType() {
        return type;
    }

    public String getRoot() {
        return root;
    }

    public String getMountPoint() {
        return mountPoint;
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }
}
