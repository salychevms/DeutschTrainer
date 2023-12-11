package de.salychevms.deutschtrainer.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeRuTest {

    @Test
    void testConstructorWithParameters() {
        Deutsch testDeutsch=new Deutsch();
        Russian testRussian=new Russian();
        DeRu deRu = new DeRu(testDeutsch, testRussian);
        assertEquals(testDeutsch, deRu.getDeutsch());
        assertEquals(testRussian, deRu.getRussian());
    }

    @Test
    void testToString() {
        Deutsch testDeutsch=new Deutsch();
        Russian testRussian=new Russian();
        DeRu deRu = new DeRu(testDeutsch, testRussian);
        assertEquals("De-Ru pair{" +
                "id=" + deRu.getId() + '\'' +
                ", deutsch=" + testDeutsch.getId() + '\'' +
                ", russian=" + testRussian.getId() + '\'' +
                '}', deRu.toString());
    }

    @Test
    void testSetAndGetId() {
        DeRu deRu=new DeRu();
        deRu.setId(54321987654321L);
        assertEquals(54321987654321L, deRu.getId());
    }

    @Test
    void testSetAndGetDeutsch() {
        Deutsch testDeutsch=new Deutsch();
        DeRu deRu=new DeRu();
        deRu.setDeutsch(testDeutsch);
        assertEquals(testDeutsch, deRu.getDeutsch());
    }

    @Test
    void testSetAndGetRussian() {
        Russian testRussian=new Russian();
        DeRu deRu=new DeRu();
        deRu.setRussian(testRussian);
        assertEquals(testRussian, deRu.getRussian());
    }
}