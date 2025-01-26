package de.salychevms.deutschtrainer.Models;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Users;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UsersTest {

    @Test
    void testUsersConstructorWithParameters() {
        Long testTelegramId = 123456789000000L;
        Date testDate = new Date();
        String testUserName = "testUserName";
        Users user = new Users(testTelegramId, testUserName, testDate);
        assertEquals(testUserName, user.getUserName());
        assertEquals(testTelegramId, user.getTelegramId());
        assertEquals(testDate, user.getRegistrationDate());
    }

    @Test
    void testToString() {
        Long testTelegramId = 123456789000000L;
        Date testDate = new Date();
        String testUserName = "testUserName";
        Users user = new Users(testTelegramId, testUserName, testDate);
        assertEquals("Users: " +
                "telegramId=\t" + testTelegramId + '\n' +
                ", name=\t" + null + '\n' +
                ", surname=\t" + null + '\n' +
                ", userName=\t" + testUserName + '\n' +
                ", phoneNumber=\t" + null + '\n' +
                ", registrationDate=\t" + testDate + '\n' +
                ", admin=\t" + false, user.toString());
        System.out.println(user);
    }

    @Test
    void testSetAndGetTelegramId() {
        Users user = new Users();
        user.setTelegramId(1234567890000000L);
        assertEquals(1234567890000000L, user.getTelegramId());
    }

    @Test
    void testSetAndGetName() {
        Users user = new Users();
        user.setName("TestName");
        assertEquals("TestName", user.getName());
    }

    @Test
    void testSetAndGetSurname() {
        Users user = new Users();
        user.setSurname("TestSurname");
        assertEquals("TestSurname", user.getSurname());
    }

    @Test
    void testSetAndGetUserName() {
        Users user = new Users();
        user.setUserName("TestUserName");
        assertEquals("TestUserName", user.getUserName());
    }

    @Test
    void testSetAndGetPhoneNumber() {
        Users user = new Users();
        user.setPhoneNumber("+491571234567");
        assertEquals("+491571234567", user.getPhoneNumber());
    }

    @Test
    void testSetAndGetRegistrationDate() {
        Users user = new Users();
        Date testDate = new Date();
        user.setRegistrationDate(testDate);
        assertEquals(testDate, user.getRegistrationDate());
    }

    @Test
    void testSetTrueOrFalseIsAdminStatusAndGet() {
        Users user = new Users();
        //check default status
        assertFalse(user.isAdmin());

        user.setAdmin(true);
        assertTrue(user.isAdmin());
        user.setAdmin(false);
        assertFalse(user.isAdmin());
    }
}