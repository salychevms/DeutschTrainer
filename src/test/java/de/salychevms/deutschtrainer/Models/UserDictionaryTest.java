package de.salychevms.deutschtrainer.Models;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.DeRuPairs;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserDictionary;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserLanguage;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserDictionaryTest {

    @Test
    void testConstructorWithParameters() {
        UserLanguage testUserLanguage = new UserLanguage();
        DeRuPairs testPair = new DeRuPairs();
        Date testDate = new Date();
        UserDictionary userDictionary = new UserDictionary(testUserLanguage, testPair, testDate);
        assertEquals(testUserLanguage, userDictionary.getUserLanguage());
        assertEquals(testPair, userDictionary.getPair());
        assertEquals(testDate, userDictionary.getDateAdded());
    }

    @Test
    void testToString() {
        UserLanguage testUserLanguage = new UserLanguage();
        DeRuPairs testPair = new DeRuPairs();
        Date testDate = new Date();
        UserDictionary userDictionary = new UserDictionary(testUserLanguage, testPair, testDate);
        assertEquals("UserDictionary{" +
                "id=" + userDictionary.getId() + '\'' +
                ", userLanguage=" + testUserLanguage.getId() + '\'' +
                ", pair=" + testPair.getId() + '\'' +
                ", additionDate=" + testDate + '\'' +
                '}', userDictionary.toString());
    }

    @Test
    void testSetAndGetId() {
        UserDictionary userDictionary = new UserDictionary();
        userDictionary.setId(123456543210000L);
        assertEquals(123456543210000L, userDictionary.getId());
    }

    @Test
    void testSetAndGetUserLanguage() {
        UserLanguage testUserLanguage = new UserLanguage();
        UserDictionary userDictionary = new UserDictionary();
        userDictionary.setUserLanguage(testUserLanguage);
        assertEquals(testUserLanguage, userDictionary.getUserLanguage());
    }

    @Test
    void testSetAndGetPair() {
        DeRuPairs testPair = new DeRuPairs();
        UserDictionary userDictionary = new UserDictionary();
        userDictionary.setPair(testPair);
        assertEquals(testPair, userDictionary.getPair());
    }

    @Test
    void testSetAndGetDateAdded() {
        Date testDate = new Date();
        UserDictionary userDictionary = new UserDictionary();
        userDictionary.setDateAdded(testDate);
        assertEquals(testDate, userDictionary.getDateAdded());
    }
}