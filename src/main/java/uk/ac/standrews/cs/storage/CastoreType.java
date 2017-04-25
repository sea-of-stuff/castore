package uk.ac.standrews.cs.storage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public enum CastoreType {

    LOCAL("local"),
    NETWORK("network"),
    DROPBOX("dropbox"),
    MEMORY("memory"),
    REDIS("redis"),
    AWS_S3("aws_s3");

    private final String text;

    CastoreType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    public static CastoreType getEnum(String value) {
        for(CastoreType v : values())
            if(v.toString().equalsIgnoreCase(value)) return v;
        throw new IllegalArgumentException();
    }
}
