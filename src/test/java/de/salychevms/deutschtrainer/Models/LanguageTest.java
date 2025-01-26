package de.salychevms.deutschtrainer.Models;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Language;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LanguageTest {

    @Test
    void testToString() {
        Language language = new Language();
        language.setId(123123L);
        language.setName("testName");
        language.setIdentifier("testIdentifier");
        assertEquals("Language{" +
                "id=" + 123123L + '\'' +
                ", name='" + "testName" + '\'' +
                ", identifier='" + "testIdentifier" + '\'' +
                '}', language.toString());
    }

    @Test
    void testSetAndGetId() {
        Language language = new Language();
        language.setId(1234567890000L);
        assertEquals(1234567890000L, language.getId());
    }

    @Test
    void testSetAndGetName() {
        Language language = new Language();
        language.setName("testLanguageName");
        assertEquals("testLanguageName", language.getName());
    }

    @Test
    void testSetAndGetIdentifier() {
        Language language = new Language();
        language.setIdentifier("testIdentifier");
        assertEquals("testIdentifier", language.getIdentifier());
    }
}