package de.salychevms.deutschtrainer.Models;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Deutsch;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeutschTest {

    @Test
    void testConstructorWithParameters() {
        Deutsch deutsch = new Deutsch("TestDeWord");
        assertEquals("TestDeWord", deutsch.getDeWord());
    }

    @Test
    void testToString() {
        Deutsch deutsch = new Deutsch("TestDeWord");
        assertEquals("Deutsch{" +
                "id=" + deutsch.getId() + '\'' +
                ", word='" + "TestDeWord" + '\'' +
                '}', deutsch.toString());
    }

    @Test
    void testSetAndGetId() {
        Deutsch deutsch=new Deutsch();
        deutsch.setId(99887766550000L);
        assertEquals(99887766550000L, deutsch.getId());
    }

    @Test
    void testSetAndGetDeWord() {
        Deutsch deutsch=new Deutsch();
        deutsch.setDeWord("testDeWord");
        assertEquals("testDeWord", deutsch.getDeWord());
    }
}