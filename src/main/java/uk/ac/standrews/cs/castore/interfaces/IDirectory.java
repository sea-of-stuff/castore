package uk.ac.standrews.cs.castore.interfaces;

import uk.ac.standrews.cs.castore.exceptions.BindingAbsentException;

import java.util.Iterator;

/**
 *
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public interface IDirectory extends StatefulObject {

    StatefulObject get(String name) throws BindingAbsentException;

    boolean contains(String name);

    void remove(String name) throws BindingAbsentException;

    /**
     * An iterator of the Stateful objects contained in this directory.
     * @return
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
