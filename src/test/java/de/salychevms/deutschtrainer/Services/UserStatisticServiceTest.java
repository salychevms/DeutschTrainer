package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.UserDictionary;
import de.salychevms.deutschtrainer.Models.UserStatistic;
import de.salychevms.deutschtrainer.Repo.UserStatisticRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
        Optional<UserStatistic> result = userStatisticService.getUserStatisticByWord(wordPair);

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
        assertEquals(statistics,result);
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
        Date lastTrainingDate=new Date();
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
        UserStatistic statistic=new UserStatistic();
        UserStatistic someOneStatistic=new UserStatistic();
        UserStatistic nextStatistic=new UserStatistic();
        List<UserStatistic> statisticList=new ArrayList<>();
        statisticList.add(statistic);
        statisticList.add(someOneStatistic);
        statisticList.add(nextStatistic);

        when(userStatisticRepository.findAll()).thenReturn(statisticList);
        List<UserStatistic> result=userStatisticService.findAll();
        assertFalse(result.isEmpty());
        assertEquals(statisticList, result);
    }
}