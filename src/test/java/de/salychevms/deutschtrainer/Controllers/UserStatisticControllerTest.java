package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.*;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.*;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.UserStatisticService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStatisticControllerTest {
    @Mock
    private UserStatisticService userStatisticService;
    @Mock
    private UserDictionaryController userDictionaryController;
    @Mock
    private DeRuPairsController deRuPairsController;
    @Mock
    private DeutschController deutschController;
    @Mock
    private RussianController russianController;
    @InjectMocks
    private UserStatisticController userStatisticController;

    @Test
    void testGetAllStatisticWithNewWords() {
        UserLanguage userLanguage = new UserLanguage();
        userLanguage.setId(123L);
        UserDictionary userDictionary1 = new UserDictionary(userLanguage, new DeRuPairs(), new Date());
        UserDictionary userDictionary2 = new UserDictionary(userLanguage, new DeRuPairs(), new Date());
        userDictionary1.setId(45L);
        userDictionary2.setId(67L);
        UserStatistic us1 = new UserStatistic(userDictionary1);
        UserStatistic us2 = new UserStatistic(userDictionary2);
        List<UserStatistic> userStatistics = new ArrayList<>();
        userStatistics.add(us1);
        userStatistics.add(us2);

        when(userStatisticService.getUserStatisticNewWordIsTrue()).thenReturn(userStatistics);
        when(userDictionaryController.getById(us1.getWord().getId())).thenReturn(Optional.of(userDictionary1));
        when(userDictionaryController.getById(us2.getWord().getId())).thenReturn(Optional.of(userDictionary2));

        List<UserStatistic> result = userStatisticController.getAllStatisticWithNewWords(userLanguage);

        assertEquals(2, result.size());
        assertEquals(userStatistics, result);
    }

    @Test
    void testGetAllStatisticWithFailsAllDesc() {
        UserLanguage userLanguage = new UserLanguage();
        userLanguage.setId(321L);
        UserDictionary ud1 = new UserDictionary(userLanguage, new DeRuPairs(), new Date());
        UserDictionary ud2 = new UserDictionary(userLanguage, new DeRuPairs(), new Date());
        UserDictionary ud3 = new UserDictionary(userLanguage, new DeRuPairs(), new Date());
        ud1.setId(987L);
        ud2.setId(98L);
        ud3.setId(9L);
        UserStatistic us1 = new UserStatistic(ud1);
        UserStatistic us2 = new UserStatistic(ud2);
        UserStatistic us3 = new UserStatistic(ud3);
        List<UserStatistic> userStatistics = new ArrayList<>();
        userStatistics.add(us1);
        userStatistics.add(us2);
        userStatistics.add(us3);

        when(userStatisticService.getUserStatisticSortByFailsAllDesc()).thenReturn(userStatistics);
        when(userDictionaryController.getById(us1.getWord().getId())).thenReturn(Optional.of(ud1));
        when(userDictionaryController.getById(us2.getWord().getId())).thenReturn(Optional.of(ud2));
        when(userDictionaryController.getById(us3.getWord().getId())).thenReturn(Optional.of(ud3));
        List<UserStatistic> result = userStatisticController.getAllStatisticWithFailsAllDesc(userLanguage);

        assertEquals(3, result.size());
        assertEquals(userStatistics, result);
    }

    @Test
    void testGetAllStatisticWithIterationsAllAsc() {
        UserLanguage userLanguage = new UserLanguage();
        userLanguage.setId(686L);
        UserDictionary ud1 = new UserDictionary(userLanguage, new DeRuPairs(), new Date());
        UserDictionary ud2 = new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());
        UserDictionary ud3 = new UserDictionary(userLanguage, new DeRuPairs(), new Date());
        ud1.setId(777L);
        ud2.setId(69L);
        ud3.setId(1L);
        UserStatistic us1 = new UserStatistic(ud1);
        UserStatistic us2 = new UserStatistic(ud2);
        UserStatistic us3 = new UserStatistic(ud3);
        List<UserStatistic> userStatistics = new ArrayList<>();
        userStatistics.add(us1);
        userStatistics.add(us2);
        userStatistics.add(us3);

        when(userStatisticService.getUserStatisticSortByFailsAllDesc()).thenReturn(userStatistics);
        when(userDictionaryController.getById(us1.getWord().getId())).thenReturn(Optional.of(ud1));
        when(userDictionaryController.getById(us2.getWord().getId())).thenReturn(Optional.of(ud2));
        when(userDictionaryController.getById(us3.getWord().getId())).thenReturn(Optional.of(ud3));
        List<UserStatistic> result = userStatisticController.getAllStatisticWithFailsAllDesc(userLanguage);

        assertEquals(2, result.size());
        assertNotEquals(userStatistics, result);
        assertEquals(us1, result.get(0));
        assertEquals(us3, result.get(1));
    }

    @Test
    void testGetUserStatisticByUserDictionaryIfExists() {
        Long userDictionaryId = 789L;
        Long userLanguageId = 678L;
        UserLanguage userLanguage = new UserLanguage(new Users(), new Language());
        userLanguage.setId(userLanguageId);
        UserDictionary userDictionary = new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());
        userDictionary.setUserLanguage(userLanguage);
        userDictionary.setId(userDictionaryId);
        UserStatistic userStatistic = new UserStatistic(userDictionary);

        when(userStatisticService.getUserStatisticByUserDictionary(userDictionary)).thenReturn(Optional.of(userStatistic));
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

        when(userStatisticService.getUserStatisticByUserDictionary(userDictionary)).thenReturn(Optional.empty());
        Optional<UserStatistic> result = userStatisticController.getUserStatisticByUserDictionary(userDictionary, new UserLanguage());

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testSaveNewPairInStatistic() {
        UserDictionary userDictionary = new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());

        userStatisticController.saveNewPairInStatistic(userDictionary);
        verify(userStatisticService, times(1)).saveNewWordInStatistic(userDictionary);
    }

    @Test
    void testUpdateAllIterationsAndNewWordStatus() {
        UserDictionary userDictionary = new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());

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
        UserDictionary userDictionary = new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());

        userStatisticController.updateAllFails(userDictionary);
        verify(userStatisticService, times(1)).updateMonthFails(userDictionary);
        verify(userStatisticService, times(1)).updateWeekFails(userDictionary);
        verify(userStatisticService, times(1)).updateDayFails(userDictionary);
        verify(userStatisticService, times(1)).updateAllFails(userDictionary);
    }

    @Test
    void testSetFailStatusTrueAndFalseAndGetFailStatus() {
        UserDictionary userDictionary = new UserDictionary();

        userStatisticController.setFailStatusFalse(userDictionary);
        verify(userStatisticService, times(1)).setFailStatusFalse(userDictionary);

        userStatisticController.setFailStatusTrue(userDictionary);
        verify(userStatisticService, times(1)).setFailStatusTrue(userDictionary);
    }

    @Test
    public void testGetBasicStatistic() {
        Long telegramId = 123L;
        Date date=new Date();
        String languageIdentifier = "DE";
        Deutsch deutsch = new Deutsch("Haus");
        deutsch.setId(153L);
        Russian russian = new Russian("дом");
        russian.setId(9784L);
        DeRuPairs pairs = new DeRuPairs(deutsch, russian);
        pairs.setId(5555L);
        UserDictionary userDictionary = new UserDictionary(new UserLanguage(), pairs, new Date());
        userDictionary.setId(8946566L);
        UserStatistic userStatistic = new UserStatistic(userDictionary);
        userStatistic.setFailsAll(10L);
        userStatistic.setId(65456454L);
        List<UserStatistic> statistics = new ArrayList<>();
        statistics.add(userStatistic);

        when(userStatisticService.findLastTrainingForUserAndLanguage(telegramId, languageIdentifier)).thenReturn(date);
        when(userDictionaryController.getCountUniqueGermanWordsForTelegramIdAndLanguageIdentifier(telegramId, "DE")).thenReturn(1);
        when(userDictionaryController.getCountPairsForUserAndLanguageIdentifier(telegramId, "DE")).thenReturn(10);
        when(userStatisticService.getCountPairsWithNewWordForUserAndLanguage(telegramId, "DE")).thenReturn(20);
        when(userStatisticService.findWordsWithMaxFailsAllForUserAndLanguageIdentifier(telegramId, "DE")).thenReturn(statistics);
        when(userDictionaryController.getById(userStatistic.getWord().getId())).thenReturn(Optional.of(userDictionary));
        when(deRuPairsController.getDeRuById(userDictionary.getPair().getId())).thenReturn(Optional.of(pairs));
        when(deutschController.findById(deutsch.getId())).thenReturn(deutsch);
        when(russianController.findById(russian.getId())).thenReturn(russian);
        String result=userStatisticController.getBasicStatistic(telegramId);

        String expected = "В Вашем словаре 1 немецких уникальных слов.\n" +
                "\nС этими словами у Вас 10 пар.\n" +
                "\nИз них 20 новых пар, которые вам еще предстоит учить.\n" +
                "\nПара(ы) слов, в которой(ых) вы чаще всего ошибаетесь:\n" +
                "\nHaus - дом\n" +
                "\nКоличество ошибок с этой парой = 10" +
                "\n\nВаша последняя тренировка была: сегодня\n\n";

        assertEquals(expected, result );
    }

    @Test
    void testDecreaseFailTraining() {
        UserDictionary userDictionary = new UserDictionary();
        Long userDictionaryId = 123456789L;
        userDictionary.setId(userDictionaryId);

        userStatisticService.decreaseFailTraining(userDictionary);
        verify(userStatisticService, times(1)).decreaseFailTraining(userDictionary);
    }
}