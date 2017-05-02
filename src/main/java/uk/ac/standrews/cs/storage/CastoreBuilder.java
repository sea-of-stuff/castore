package uk.ac.standrews.cs.storage;

/**
 * This class is used to define the settings needed to create the Storage object
 *
 * Fluent pattern used for this class
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class CastoreBuilder {

    static final int NOT_SET = -1;

    // All
    private CastoreType type;
    private String root;

    // Network
    private String mountPoint;

    // Redis
    private String hostname;
    private int port = NOT_SET;

    // Dropbox
    private String token;

    // AWS
    private String accessKey;
    private String secretAccessKey;

    // NOTE: Still unused params
    private boolean useChunkUpload;
    private int chunkThreshold;
    private int chunkSize;

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

    public CastoreBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public CastoreBuilder setAccessKey(String accessKey) {
        this.accessKey = accessKey;
        return this;
    }

    public CastoreBuilder setSecretAccessKey(String secretAccessKey) {
        this.secretAccessKey = secretAccessKey;
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

    public String getToken() {
        return token;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretAccessKey() {
        return secretAccessKey;
    }
}
