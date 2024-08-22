package de.salychevms.deutschtrainer.Models;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class UserStatisticTest {

    @Test
    void testConstructorWithParameters(){
        UserDictionary testUserPair=new UserDictionary();
        UserStatistic userStatistic=new UserStatistic(testUserPair);
        assertEquals(testUserPair, userStatistic.getWord());
        assertTrue(userStatistic.isNewWord());
    }

    @Test
    void testToString() {
        UserDictionary testUserPair=new UserDictionary();
        Date testDate=new Date();
        UserStatistic userStatistic=new UserStatistic(testUserPair);
        userStatistic.setFailsAll(5L);
        userStatistic.setIterationsPerMonth(10L);
        userStatistic.setLastTraining(testDate);
        assertEquals("UserStatistic{" +
                "Id=" + userStatistic.getId() +'\'' +
                ", word=" + testUserPair.getId() +'\'' +
                ", newWord=" + true +'\'' +
                ", iterationsAll=" + null +'\'' +
                ", iterationsPerMonth=" + 10L +'\'' +
                ", iterationsPerWeek=" + null +'\'' +
                ", iterationsPerDay=" + null +'\'' +
                ", failTraining=" + null +'\'' +
                ", failStatus=" + false +'\'' +
                ", failsAll=" + 5L +'\'' +
                ", failsPerMonth=" + null +'\'' +
                ", failsPerWeek=" + null +'\'' +
                ", failsPerDay=" + null +'\'' +
                ", lastTraining=" + testDate +'\'' +
                '}', userStatistic.toString());
    }

    @Test
    void testSetAndGetId() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setId(1234567890000L);
        assertEquals(1234567890000L, userStatistic.getId());
    }

    @Test
    void testSetAndGetWord() {
        UserDictionary testUserPair=new UserDictionary();
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setWord(testUserPair);
        assertEquals(testUserPair, userStatistic.getWord());
    }

    @Test
    void testSetAndIsNewWord() {
        UserStatistic userStatistic=new UserStatistic(new UserDictionary());
        //test basic parameter - 'true'
        assertTrue(userStatistic.isNewWord());

        userStatistic.setNewWord(false);
        assertFalse(userStatistic.isNewWord());
        userStatistic.setNewWord(true);
        assertTrue(userStatistic.isNewWord());
    }

    @Test
    void testSetAndGetIterationsAll() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setIterationsAll(20L);
        assertEquals(20L, userStatistic.getIterationsAll());
    }

    @Test
    void testSetAndGetIterationsPerMonth() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setIterationsPerMonth(10L);
        assertEquals(10L, userStatistic.getIterationsPerMonth());
    }

    @Test
    void testSetAndGetIterationsPerWeek() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setIterationsPerWeek(5L);
        assertEquals(5L, userStatistic.getIterationsPerWeek());
    }

    @Test
    void testSetAndGetIterationsPerDay() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setIterationsPerDay(3L);
        assertEquals(3L, userStatistic.getIterationsPerDay());
    }

    @Test
    void testSetAndGetFailsAll() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setFailsAll(20L);
        assertEquals(20L, userStatistic.getFailsAll());
    }

    @Test
    void testSetAndGetFailsPerMonth() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setFailsPerMonth(10L);
        assertEquals(10L, userStatistic.getFailsPerMonth());
    }

    @Test
    void testSetAndGetFailsPerWeek() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setFailsPerWeek(5L);
        assertEquals(5L, userStatistic.getFailsPerWeek());
    }

    @Test
    void testSetAndGetFailsPerDay() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setFailsPerDay(2L);
        assertEquals(2L, userStatistic.getFailsPerDay());
    }

    @Test
    void testSetAndGetLastTraining() {
        Date testDate=new Date();
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setLastTraining(testDate);
        assertEquals(testDate, userStatistic.getLastTraining());
    }

    @Test
    void testSetAndGetFailTraining() {
        UserStatistic userStatistic=new UserStatistic();
        userStatistic.setFailTraining(5);

        assertEquals(5, userStatistic.getFailTraining());
        userStatistic.setFailTraining(3);
        assertEquals(3, userStatistic.getFailTraining());
    }

    @Test
    void testSetAndIsFailStatus() {
        UserStatistic userStatistic=new UserStatistic();
        //basically is false
        assertFalse(userStatistic.isFailStatus());

        userStatistic.setFailStatus(true);
        assertTrue(userStatistic.isFailStatus());
        userStatistic.setFailStatus(false);
        assertFalse(userStatistic.isFailStatus());
    }
}