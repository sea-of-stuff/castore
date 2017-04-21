package uk.ac.standrews.cs.storage.interfaces;

import uk.ac.standrews.cs.storage.exceptions.BindingAbsentException;

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
}
