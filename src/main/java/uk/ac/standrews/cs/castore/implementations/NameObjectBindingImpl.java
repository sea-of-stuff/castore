package uk.ac.standrews.cs.castore.implementations;

import uk.ac.standrews.cs.castore.interfaces.NameObjectBinding;
import uk.ac.standrews.cs.castore.interfaces.StatefulObject;

/**
 * Implements a binding between a logical name and an StatefulObject
 * A Directory contains a collection of these bindings.
 *
 * @author al
 */
public class NameObjectBindingImpl implements NameObjectBinding {

    private final String name;
    private final StatefulObject obj;

    /**
     * Creates a binding between a name and a GUID.
     *
     * @param name the name
     * @param obj an StatefulObject
     */
    public NameObjectBindingImpl(String name, StatefulObject obj) {
        this.name = name;
        this.obj = obj;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the StatefulObject.
     *
     * @return the StatefulObject
     */
    public StatefulObject getObject() {
        return obj;
    }

}

