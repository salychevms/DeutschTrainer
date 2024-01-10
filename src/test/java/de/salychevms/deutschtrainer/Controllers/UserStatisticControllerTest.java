package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Services.UserStatisticService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStatisticControllerTest {
    @Mock
    private UserStatisticService userStatisticService;
    @InjectMocks
    private UserStatisticController userStatisticController;

    @Test
    void testGetAllStatisticWithNewWords() {
        List<UserStatistic> userStatistics = new ArrayList<>();
        userStatistics.add(new UserStatistic());
        userStatistics.add(new UserStatistic(new UserDictionary()));
        userStatistics.add(new UserStatistic(new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date())));

        when(userStatisticService.getUserStatisticNewWordIsTrue()).thenReturn(userStatistics);
        List<UserStatistic> result = userStatisticController.getAllStatisticWithNewWords();

        assertNotNull(result);
        assertEquals(userStatistics, result);
    }

    @Test
    void testGetAllStatisticWithFailsAllDesc() {
        List<UserStatistic> userStatistics = new ArrayList<>();
        userStatistics.add(new UserStatistic());
        userStatistics.add(new UserStatistic(new UserDictionary()));
        userStatistics.add(new UserStatistic(new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date())));

        when(userStatisticService.getUserStatisticSortByFailsAllDesc()).thenReturn(userStatistics);
        List<UserStatistic> result = userStatisticController.getAllStatisticWithFailsAllDesc();

        assertNotNull(result);
        assertEquals(userStatistics, result);
    }

    @Test
    void testGetAllStatisticWithIterationsAllAsc() {
        List<UserStatistic> userStatistics = new ArrayList<>();
        userStatistics.add(new UserStatistic());
        userStatistics.add(new UserStatistic(new UserDictionary()));
        userStatistics.add(new UserStatistic(new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date())));

        when(userStatisticService.getUserStatisticSortByIterationAllAsc()).thenReturn(userStatistics);
        List<UserStatistic> result = userStatisticController.getAllStatisticWithIterationsAllAsc();

        assertNotNull(result);
        assertEquals(userStatistics, result);
    }

    @Test
    void testGetUserStatisticByUserDictionaryIfExists() {
        Long userDictionaryId = 789L;
        UserLanguage userLanguage = new UserLanguage(new Users(), new Language());
        UserDictionary userDictionary = new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());
        userDictionary.setUserLanguage(userLanguage);
        userDictionary.setId(userDictionaryId);
        UserStatistic userStatistic = new UserStatistic(userDictionary);

        when(userStatisticService.getUserStatisticByWord(userDictionary)).thenReturn(Optional.of(userStatistic));
        Optional<UserStatistic> result = userStatisticController.getUserStatisticByUserDictionary(userDictionary, userLanguage);

        result.ifPresent(value -> assertEquals(userStatistic, value));
    }

    @Test
    void testGetUserStatisticByUserDictionaryDoesNotExist() {
        Long userDictionaryId = 789L;
        UserLanguage userLanguage = new UserLanguage(new Users(), new Language());
        UserDictionary userDictionary = new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());
        userDictionary.setUserLanguage(userLanguage);
        userDictionary.setId(userDictionaryId);
        UserStatistic userStatistic = new UserStatistic(userDictionary);

        when(userStatisticService.getUserStatisticByWord(userDictionary)).thenReturn(Optional.empty());
        Optional<UserStatistic> result = userStatisticController.getUserStatisticByUserDictionary(userDictionary, new UserLanguage());

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testSaveNewPairInStatistic() {
        UserDictionary userDictionary=new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());

        userStatisticController.saveNewPairInStatistic(userDictionary);
        verify(userStatisticService, times(1)).saveNewWordInStatistic(userDictionary);
    }

    @Test
    void testUpdateAllIterationsAndNewWordStatus() {
        UserDictionary userDictionary=new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());

        userStatisticController.updateAllIterationsAndNewWordStatus(userDictionary);
        verify(userStatisticService, times(1)).updateLastTraining(userDictionary);
        verify(userStatisticService, times(1)).updateAllIteration(userDictionary);
        verify(userStatisticService, times(1)).updateDayIteration(userDictionary);
        verify(userStatisticService, times(1)).updateWeekIteration(userDictionary);
        verify(userStatisticService, times(1)).updateMonthIteration(userDictionary);
        verify(userStatisticService, times(1)).updateStatusNewWordByUserDictionary(userDictionary);
    }

    @Test
    void testUpdateAllFails() {
        UserDictionary userDictionary=new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());

        userStatisticController.updateAllFails(userDictionary);
        verify(userStatisticService, times(1)).updateMonthFails(userDictionary);
        verify(userStatisticService, times(1)).updateWeekFails(userDictionary);
        verify(userStatisticService, times(1)).updateDayFails(userDictionary);
        verify(userStatisticService, times(1)).updateAllFails(userDictionary);
    }
}