package uk.ac.standrews.cs.castore.interfaces;

import uk.ac.standrews.cs.castore.exceptions.BindingAbsentException;

import java.util.Iterator;

/**
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface IDirectory extends StatefulObject {

    /**
     * Get the object contained in the directory that matches the given name
     *
     * Directories should have a trailing slash, e.g. get("folder/")
     *
     * @param name
     * @return
     * @throws BindingAbsentException if the object is not present or it could not be created
     */
    StatefulObject get(String name) throws BindingAbsentException;

    /**
     * Check if the object matching the name exists inside this directory
     *
     * @param name
     * @return
     */
    boolean contains(String name);

    /**
     * Removes the object matching the specified name
     *
     * @param name
     * @throws BindingAbsentException
     */
    void remove(String name) throws BindingAbsentException;

    /**
     * An iterator of the Stateful objects contained in this directory.
     *
     * @return an iterator of NameObjectBinding
     */
    Iterator<NameObjectBinding> getIterator();

    @Override
    default long getSize() {
        long size = 0;

        Iterator<NameObjectBinding> it = getIterator();
        while(it.hasNext()) {
            StatefulObject obj = it.next().getObject();
            size += obj.getSize();
        }
        return size;
    }
}
