package de.salychevms.deutschtrainer.Models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserLanguageTest {

    @Test
    void testConstructorWithParameters(){
        Users testUser=new Users();
        Language testLanguage=new Language();
        UserLanguage userLanguage=new UserLanguage(testUser,testLanguage);
        assertEquals(testUser, userLanguage.getUser());
        assertEquals(testLanguage, userLanguage.getLanguage());

    }
    @Test
    void testToString() {
        Users testUser=new Users();
        Language testLanguage=new Language();
        UserLanguage userLanguage=new UserLanguage(testUser,testLanguage);
        assertEquals(testUser, userLanguage.getUser());
        assertEquals(testLanguage, userLanguage.getLanguage());
        assertEquals("UserLanguage{" +
                "id=" + userLanguage.getId() + '\'' +
                ", users=" + testUser.getTelegramId() + '\'' +
                ", language=" + testLanguage.getId() + '\'' +
                '}', userLanguage.toString());
    }

    @Test
    void testSetAndGetId() {
        UserLanguage userLanguage=new UserLanguage();
        userLanguage.setId(9876543210000L);
        assertEquals(9876543210000L, userLanguage.getId());
    }

    @Test
    void testSetAndGetUser() {
        Users testUser=new Users();
        UserLanguage userLanguage=new UserLanguage();
        userLanguage.setUser(testUser);
        assertEquals(testUser, userLanguage.getUser());
    }

    @Test
    void testSetAndGetLanguage() {
        Language testLanguage=new Language();
        UserLanguage userLanguage=new UserLanguage();
        userLanguage.setLanguage(testLanguage);
        assertEquals(testLanguage, userLanguage.getLanguage());
    }
}