package uk.ac.standrews.cs.storage.implementations;

import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Simone I. Conte "sic2@st-andrews.ac.uk"
 */
public class CommonStatefulObjectTest {

    @Test
    public void testNameIsLegalFalse() throws Exception {

        assertFalse(CommonStatefulObject.NameIsLegal(null));
        assertFalse(CommonStatefulObject.NameIsLegal(""));
        assertFalse(CommonStatefulObject.NameIsLegal(":"));
        assertFalse(CommonStatefulObject.NameIsLegal("\0"));
        assertFalse(CommonStatefulObject.NameIsLegal("<"));
        assertFalse(CommonStatefulObject.NameIsLegal(">"));
        assertFalse(CommonStatefulObject.NameIsLegal("\""));
        assertFalse(CommonStatefulObject.NameIsLegal("\\|"));
        assertFalse(CommonStatefulObject.NameIsLegal("?"));
        assertFalse(CommonStatefulObject.NameIsLegal("\\*"));
    }

    @Test
    public void testNameIsLegalTrue() throws Exception {

        assertTrue(CommonStatefulObject.NameIsLegal("a"));
        assertTrue(CommonStatefulObject.NameIsLegal("abc"));
        assertTrue(CommonStatefulObject.NameIsLegal("1"));
        assertTrue(CommonStatefulObject.NameIsLegal("123"));
        assertTrue(CommonStatefulObject.NameIsLegal("abc123"));
        assertTrue(CommonStatefulObject.NameIsLegal("TEST"));
        assertTrue(CommonStatefulObject.NameIsLegal("test@mail.com"));
        assertTrue(CommonStatefulObject.NameIsLegal("{[(filename)]}"));
        assertTrue(CommonStatefulObject.NameIsLegal("!+-_';,.=&^%$£@!€§±~`"));
    }
}