package uk.ac.standrews.cs.storage.implementations;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public abstract class CommonStatefulObject {

    public abstract String getPathname();

    @Override
    public String toString() {
        return getPathname();
    }
}
