package uk.ac.standrews.cs.storage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class CastoreBuilder {

    public static final int NOT_SET = -1;

    private CastoreType type;
    private String root;
    private String mountPoint;
    private String hostname;
    private int port = NOT_SET;

    public CastoreBuilder setType(CastoreType type) {
        this.type = type;
        return this;
    }

    public CastoreBuilder setRoot(String root) {
        this.root = root;
        return this;
    }

    public CastoreBuilder setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
        return this;
    }

    public CastoreBuilder setHostname(String hostname) {
        this.hostname = hostname;
        return this;
    }

    public CastoreBuilder setPort(int port) {
        this.port = port;
        return this;
    }

    public CastoreType getType() {
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
