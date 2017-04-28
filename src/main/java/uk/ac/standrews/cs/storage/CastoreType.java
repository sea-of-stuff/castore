package uk.ac.standrews.cs.storage;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public enum CastoreType {

    LOCAL("local"),
    NETWORK("network"),
    DROPBOX("dropbox"),
    REDIS("redis"),
    AWS_S3("aws_s3");

    // FUTURE Types: memory, onedrive, google drive, Sea of Stuff, git, mercurial, github

    private final String text;

    CastoreType(final String text) {
        this.text = text;
    }
}
