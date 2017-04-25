package uk.ac.standrews.cs.storage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class StorageBuilder {

    private StorageType type;
    private String root;
    private String mountPoint;
    private String hostname;
    private int port;

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
}
