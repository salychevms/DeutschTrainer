package de.salychevms.deutschtrainer.Models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DeRuPairsTest {

    @Test
    void testConstructorWithParameters() {
        Deutsch testDeutsch=new Deutsch();
        Russian testRussian=new Russian();
        DeRuPairs deRuPairs = new DeRuPairs(testDeutsch, testRussian);
        assertEquals(testDeutsch, deRuPairs.getDeutsch());
        assertEquals(testRussian, deRuPairs.getRussian());
    }

    @Test
    void testToString() {
        Deutsch testDeutsch=new Deutsch();
        Russian testRussian=new Russian();
        DeRuPairs deRuPairs = new DeRuPairs(testDeutsch, testRussian);
        assertEquals("De-Ru pair{" +
                "id=" + deRuPairs.getId() + '\'' +
                ", deutsch=" + testDeutsch.getId() + '\'' +
                ", russian=" + testRussian.getId() + '\'' +
                '}', deRuPairs.toString());
    }

    @Test
    void testSetAndGetId() {
        DeRuPairs deRuPairs =new DeRuPairs();
        deRuPairs.setId(54321987654321L);
        assertEquals(54321987654321L, deRuPairs.getId());
    }

    @Test
    void testSetAndGetDeutsch() {
        Deutsch testDeutsch=new Deutsch();
        DeRuPairs deRuPairs =new DeRuPairs();
        deRuPairs.setDeutsch(testDeutsch);
        assertEquals(testDeutsch, deRuPairs.getDeutsch());
    }

    @Test
    void testSetAndGetRussian() {
        Russian testRussian=new Russian();
        DeRuPairs deRuPairs =new DeRuPairs();
        deRuPairs.setRussian(testRussian);
        assertEquals(testRussian, deRuPairs.getRussian());
    }


}