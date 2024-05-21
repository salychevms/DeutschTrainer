package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Repo.UserStatisticRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserStatisticServiceTest {
    @Mock
    private UserStatisticRepository userStatisticRepository;

    @InjectMocks
    private UserStatisticService userStatisticService;

    @Test
    void getUserStatisticById() {
        Long statisticId = 616432L;
        UserStatistic userStatistic = new UserStatistic();
        userStatistic.setId(statisticId);

        when(userStatisticRepository.findById(statisticId)).thenReturn(Optional.of(userStatistic));
        Optional<UserStatistic> result = userStatisticService.getUserStatisticById(statisticId);

        result.ifPresent(value -> assertEquals(userStatistic, value));
    }

    @Test
    void getUserStatisticByWord() {
        Long userDictId = 91864318415L;
        UserDictionary wordPair = new UserDictionary();
        wordPair.setId(userDictId);
        UserStatistic userStatistic = new UserStatistic(wordPair);

        when(userStatisticRepository.findByWord(wordPair)).thenReturn(Optional.of(userStatistic));
        Optional<UserStatistic> result = userStatisticService.getUserStatisticByUserDictionary(wordPair);

        result.ifPresent(value -> assertEquals(userStatistic, value));
    }

    @Test
    void getUserStatisticSortByFailsAllDesc() {
        UserStatistic statistic = new UserStatistic();
        UserStatistic someOneStatistic = new UserStatistic();
        List<UserStatistic> statistics = new ArrayList<>();
        statistics.add(statistic);
        statistics.add(someOneStatistic);

        when(userStatisticRepository.findAllOrderByFailsAllDesc()).thenReturn(statistics);
        List<UserStatistic> result = userStatisticService.getUserStatisticSortByFailsAllDesc();

        verify(userStatisticRepository, times(1)).findAllOrderByFailsAllDesc();

        assertFalse(result.isEmpty());
        assertEquals(statistics, result);
    }

    @Test
    void getUserStatisticSortByIterationAllAsc() {
        UserStatistic statistic = new UserStatistic();
        UserStatistic someOneStatistic = new UserStatistic();
        List<UserStatistic> statistics = new ArrayList<>();
        statistics.add(statistic);
        statistics.add(someOneStatistic);

        when(userStatisticRepository.findAllOrderByIterationsAllAsc()).thenReturn(statistics);
        List<UserStatistic> result = userStatisticService.getUserStatisticSortByIterationAllAsc();

        verify(userStatisticRepository, times(1)).findAllOrderByIterationsAllAsc();

        assertFalse(result.isEmpty());
        assertEquals(statistics, result);
    }

    @Test
    void getUserStatisticNewWordIsTrueForUser() {
        UserStatistic statistic = new UserStatistic();
        UserStatistic newStatistic = new UserStatistic();
        UserStatistic someOneStatistic = new UserStatistic();

        List<UserStatistic> statistics = new ArrayList<>();
        statistics.add(statistic);
        statistics.add(newStatistic);
        statistics.add(someOneStatistic);

        when(userStatisticRepository.findAllByNewWordIsTrue()).thenReturn(statistics);
        List<UserStatistic> result = userStatisticService.getUserStatisticNewWordIsTrue();

        assertNotNull(result);
        assertEquals(statistics, result);
    }

    @Test
    void saveNewWordInStatistic() {
        Long pairId = 1231321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic userStatistic = new UserStatistic(pair);

        when(userStatisticRepository.save(any(UserStatistic.class))).thenReturn(userStatistic);
        userStatisticService.saveNewWordInStatistic(pair);
        ArgumentCaptor<UserStatistic> statisticCaptor = ArgumentCaptor.forClass(UserStatistic.class);

        verify(userStatisticRepository, times(1)).save(statisticCaptor.capture());
        UserStatistic capturedStatistic = statisticCaptor.getValue();

        assertEquals(pair, capturedStatistic.getWord());
    }

    @Test
    void testUpdateStatusNewWordByUserDictionaryIfIterationsEqualsOrMoreThan5() {
        Long pairId = 6541354L;
        Long statisticId = 99955588877L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic(pair);
        statistic.setId(statisticId);
        statistic.setNewWord(true);
        statistic.setIterationsAll(5L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateStatusNewWordByUserDictionary(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(5L, statistic.getIterationsAll());
        assertFalse(statistic.isNewWord());
    }

    @Test
    void testUpdateStatusNewWordByUserDictionaryIfIterationsLessThan5() {
        Long pairId = 6541354L;
        Long statisticId = 99955588877L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic(pair);
        statistic.setId(statisticId);
        statistic.setNewWord(true);
        statistic.setIterationsAll(4L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateStatusNewWordByUserDictionary(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, never()).save(any(UserStatistic.class));
        assertTrue(statistic.isNewWord());
    }

    @Test
    void testUpdateAllIterationWhenIterationsNotNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsAll(20L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateAllIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(21L, statistic.getIterationsAll());
    }

    @Test
    void testUpdateAllIterationWhenIterationsEqualsNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsAll(null);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateAllIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(1L, statistic.getIterationsAll());
    }

    @Test
    void testUpdateMonthIterationWhenEqualsNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsPerMonth(null);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateMonthIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(1L, statistic.getIterationsPerMonth());
    }

    @Test
    void testUpdateMonthIterationWhenEqualsNotNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsPerMonth(10L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateMonthIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(11L, statistic.getIterationsPerMonth());
    }

    @Test
    void testDeleteMonthIteration() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsPerMonth(10L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.deleteMonthIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertNull(statistic.getIterationsPerMonth());
    }

    @Test
    void testUpdateWeekIterationWhenEqualsNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsPerWeek(null);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateWeekIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(1L, statistic.getIterationsPerWeek());
    }

    @Test
    void testUpdateWeekIterationWhenEqualsNotNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsPerWeek(10L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateWeekIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(11L, statistic.getIterationsPerWeek());
    }

    @Test
    void testDeleteWeekIteration() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsPerWeek(6L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.deleteWeekIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertNull(statistic.getIterationsPerWeek());
    }

    @Test
    void testUpdateDayIterationWhenEqualsNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsPerDay(null);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateDayIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(1L, statistic.getIterationsPerDay());
    }

    @Test
    void testUpdateDayIterationWhenEqualsNotNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsPerDay(13L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateDayIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(14L, statistic.getIterationsPerDay());
    }

    @Test
    void testDeleteDayIteration() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setIterationsPerDay(4L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.deleteDayIteration(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertNull(statistic.getIterationsPerDay());
    }

    @Test
    void testUpdateAllFailsWhenEqualsNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsAll(null);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateAllFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(1L, statistic.getFailsAll());
    }

    @Test
    void testUpdateAllFailsWhenEqualsNotNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsAll(17L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateAllFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(18L, statistic.getFailsAll());
    }

    @Test
    void testUpdateMonthFailsWhenEqualsNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsPerMonth(null);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateMonthFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(1L, statistic.getFailsPerMonth());
    }

    @Test
    void testUpdateMonthFailsWhenEqualsNotNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsPerMonth(110L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateMonthFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(111L, statistic.getFailsPerMonth());
    }

    @Test
    void testDeleteMonthFails() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsPerMonth(41L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.deleteMonthFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertNull(statistic.getFailsPerMonth());
    }

    @Test
    void testUpdateWeekFailsWhenEqualsNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsPerWeek(null);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateWeekFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(1L, statistic.getFailsPerWeek());
    }

    @Test
    void testUpdateWeekFailsWhenEqualsNotNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsPerWeek(11L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateWeekFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(12L, statistic.getFailsPerWeek());
    }

    @Test
    void testDeleteWeekFails() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsPerWeek(61L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.deleteWeekFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertNull(statistic.getFailsPerWeek());
    }

    @Test
    void testUpdateDayFailsWhenEqualsNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsPerDay(null);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateDayFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(1L, statistic.getFailsPerDay());
    }

    @Test
    void testUpdateDayFailsWhenEqualsNotNull() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsPerDay(211L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateDayFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertEquals(212L, statistic.getFailsPerDay());
    }

    @Test
    void testDeleteDayFails() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        statistic.setFailsPerDay(21L);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.deleteDayFails(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertNull(statistic.getFailsPerDay());
    }

    @Test
    void testUpdateLastTraining() {
        Long statisticId = 123456789L;
        Long pairId = 987654321L;
        UserDictionary pair = new UserDictionary();
        pair.setId(pairId);
        UserStatistic statistic = new UserStatistic();
        statistic.setId(statisticId);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        calendar.add(Calendar.HOUR_OF_DAY, 20);
        calendar.add(Calendar.MINUTE, 5);
        calendar.add(Calendar.SECOND, 0);
        calendar.add(Calendar.MILLISECOND, 0);
        Date lastTrainingDate = calendar.getTime();
        statistic.setLastTraining(lastTrainingDate);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.updateLastTraining(pair);
        verify(userStatisticRepository, times(1)).findByWord(pair);

        verify(userStatisticRepository, times(1)).save(statistic);
        assertNotNull(statistic.getLastTraining());
        assertNotEquals(lastTrainingDate, statistic.getLastTraining());
    }

    @Test
    void testFindAll() {
        UserStatistic statistic = new UserStatistic();
        UserStatistic someOneStatistic = new UserStatistic();
        UserStatistic nextStatistic = new UserStatistic();
        List<UserStatistic> statisticList = new ArrayList<>();
        statisticList.add(statistic);
        statisticList.add(someOneStatistic);
        statisticList.add(nextStatistic);

        when(userStatisticRepository.findAll()).thenReturn(statisticList);
        List<UserStatistic> result = userStatisticService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(statisticList, result);
    }

    @Test
    void testSetFailStatusTrueAndFalseAndIsFailStatus() {
        UserDictionary pair = new UserDictionary();
        Long userDictionaryId = 123456789L;
        pair.setId(userDictionaryId);

        UserStatistic statistic = new UserStatistic();
        statistic.setWord(pair);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.setFailStatusTrue(pair);

        assertTrue(statistic.isFailStatus());

        userStatisticService.setFailStatusFalse(pair);

        assertFalse(statistic.isFailStatus());
    }

    @Test
    void testDecreaseFailTraining() {
        UserDictionary pair = new UserDictionary();
        Long userDictionaryId = 123456789L;
        pair.setId(userDictionaryId);

        UserStatistic statistic = new UserStatistic();
        statistic.setWord(pair);
        statistic.setFailTraining(4);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.decreaseFailTraining(pair);

        assertEquals(3, statistic.getFailTraining());

        statistic.setFailTraining(1);

        when(userStatisticRepository.findByWord(pair)).thenReturn(Optional.of(statistic));
        userStatisticService.decreaseFailTraining(pair);

        assertNull(statistic.getFailTraining());
        assertFalse(statistic.isFailStatus());
    }

    @Test
    void getCountPairsWithNewWordForUserAndLanguage() {
        Long telegramId = 41143L;
        String languageIdentifier = "DE";
        int count = 4;

        when(userStatisticRepository.countPairsWithNewWordForUserAndLanguage(telegramId, languageIdentifier)).thenReturn((long) count);
        int result = userStatisticService.getCountPairsWithNewWordForUserAndLanguage(telegramId, languageIdentifier);

        assertEquals(count, result);
    }

    @Test
    void findLastTrainingForUserAndLanguage() {
        Long telegramId = 357847L;
        String languageIdentifier = "DE";
        Date date = new Date();

        when(userStatisticRepository.findLastTrainingForUserAndLanguage(telegramId, languageIdentifier)).thenReturn(date);
        Date result = userStatisticService.findLastTrainingForUserAndLanguage(telegramId, languageIdentifier);

        assertEquals(date, result);
    }

    @Test
    void findWordsWithMaxFailsAllForUserAndLanguageIdentifier() {
        Long telegramId = 357847L;
        Long someTelegramId = 8671364L;
        String languageIdentifier = "DE";

        UserStatistic statistic = new UserStatistic();
        statistic.setFailsAll(5L);
        statistic.setWord(new UserDictionary(new UserLanguage(new Users(), new Language("n")), new DeRuPairs(new Deutsch("d"), new Russian("r")), new Date()));

        UserStatistic someOneStatistic = new UserStatistic();
        someOneStatistic.setFailsAll(3L);
        someOneStatistic.setWord(new UserDictionary(new UserLanguage(new Users(), new Language("n")), new DeRuPairs(new Deutsch("dd"), new Russian("rr")), new Date()));

        UserStatistic nextStatistic = new UserStatistic();
        nextStatistic.setFailsAll(3L);
        nextStatistic.setWord(new UserDictionary(new UserLanguage(new Users(), new Language("n")), new DeRuPairs(new Deutsch("dddd"), new Russian("rrrr")), new Date()));

        List<UserStatistic> statisticList = new ArrayList<>();
        statisticList.add(statistic);

        List<UserStatistic> someOneStatisticList = new ArrayList<>();
        someOneStatisticList.add(someOneStatistic);
        someOneStatisticList.add(nextStatistic);

        when(userStatisticRepository.findWordsWithMaxFailsAllForUserAndLanguageIdentifier(telegramId, languageIdentifier)).thenReturn(statisticList);
        when(userStatisticRepository.findWordsWithMaxFailsAllForUserAndLanguageIdentifier(someTelegramId, languageIdentifier)).thenReturn(someOneStatisticList);

        List<UserStatistic> result1 = userStatisticService.findWordsWithMaxFailsAllForUserAndLanguageIdentifier(telegramId, languageIdentifier);
        List<UserStatistic> result2 = userStatisticService.findWordsWithMaxFailsAllForUserAndLanguageIdentifier(someTelegramId, languageIdentifier);

        assertEquals(statisticList, result1);
        assertEquals(someOneStatisticList, result2);
    }

    @Test
    void countWordsWithFailStatusForUserAndLanguage() {
        int count = 7;
        Long telegramId = 87163L;
        String languageIdentifier = "DE";

        when(userStatisticRepository.countWordsWithFailStatusForUserAndLanguage(telegramId, languageIdentifier)).thenReturn(count);
        int result = userStatisticService.countWordsWithFailStatusForUserAndLanguage(telegramId, languageIdentifier);

        assertEquals(count, result);
    }
}