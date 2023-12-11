package de.salychevms.deutschtrainer.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RussianTest {
    @Test
    void testConstructorWithParameters() {
        Russian russian = new Russian("testRuWord");
        assertEquals("testRuWord", russian.getRuWord());
    }

    @Test
    void testToString() {
        Russian russian = new Russian("testRuWord");
        assertEquals("Russian{" +
                "id=" + russian.getId() + '\'' +
                ", word='" + "testRuWord" + '\'' +
                '}', russian.toString());
    }

    @Test
    void testSetAndGetId() {
        Russian russian = new Russian();
        russian.setId(9876543210000L);
        assertEquals(9876543210000L, russian.getId());
    }

    @Test
    void testSetAndGetWord() {
        Russian russian = new Russian();
        russian.setRuWord("testRuWord");
        assertEquals("testRuWord", russian.getRuWord());
    }
}