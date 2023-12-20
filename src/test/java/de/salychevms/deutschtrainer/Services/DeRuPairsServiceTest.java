package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.DeRuPairs;
import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Repo.DeRuPairsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeRuPairsServiceTest {
    @Mock
    private DeRuPairsRepository deRuPairsRepository;

    @InjectMocks
    private DeRuPairsService deRuPairsService;

    @Test
    void createNewPairsIfPairDoesNotExist() {
        Russian russian = new Russian("russian");
        Deutsch deutsch = new Deutsch("deutsch");
        Long deutschId = 5654615132L;
        Long russianId = 123L;
        russian.setId(russianId);
        deutsch.setId(deutschId);

        DeRuPairs deRuPairs = new DeRuPairs(deutsch, russian);
        Long deRuId = 44455556666L;
        deRuPairs.setId(deRuId);

        when(deRuPairsRepository.getByDeutschIdAndRussianId(anyLong(), anyLong())).thenReturn(Optional.empty());
        when(deRuPairsRepository.save(any(DeRuPairs.class))).thenReturn(deRuPairs);

        Long newDeRuId = deRuPairsService.createNewPairs(deutsch, russian);
        verify(deRuPairsRepository, times(1)).getByDeutschIdAndRussianId(deutschId, russianId);
        ArgumentCaptor<DeRuPairs> deRuCaptor = ArgumentCaptor.forClass(DeRuPairs.class);
        verify(deRuPairsRepository, times(1)).save(deRuCaptor.capture());
        assertEquals(deRuId, newDeRuId);
    }

    @Test
    void createNewPairsIfPairDExists() {
        Russian russian = new Russian("russian");
        Deutsch deutsch = new Deutsch("deutsch");
        Long deutschId = 5654615132L;
        Long russianId = 123L;
        russian.setId(russianId);
        deutsch.setId(deutschId);

        DeRuPairs deRuPairs = new DeRuPairs(deutsch, russian);
        Long deRuId = 44455556666L;
        deRuPairs.setId(deRuId);

        when(deRuPairsRepository.getByDeutschIdAndRussianId(deutschId, russianId)).thenReturn(Optional.of(deRuPairs));
        Long pairIdWasFound = deRuPairsService.createNewPairs(deutsch, russian);

        assertEquals(deRuId, pairIdWasFound);
    }

    @Test
    void findById() {
        DeRuPairs deRuPairs = new DeRuPairs(new Deutsch(), new Russian());
        Long deRuId = 7239652523627L;
        deRuPairs.setId(deRuId);

        when(deRuPairsRepository.findById(deRuId)).thenReturn(Optional.of(deRuPairs));
        Optional<DeRuPairs> result = deRuPairsService.findById(deRuId);

        result.ifPresent(value -> assertEquals(deRuPairs, value));
    }

    @Test
    void findByGermanIdAndRussianId() {
        Long deutschId = 10827023702L;
        Long russianId = 435786510L;
        Deutsch deutsch = new Deutsch();
        Russian russian = new Russian();
        deutsch.setId(deutschId);
        russian.setId(russianId);
        DeRuPairs deRuPairs = new DeRuPairs(deutsch, russian);
        Long deRuId = 99999000000L;
        deRuPairs.setId(deRuId);

        when(deRuPairsRepository.getByDeutschIdAndRussianId(deutschId, russianId)).thenReturn(Optional.of(deRuPairs));
        Optional<DeRuPairs> result= deRuPairsService.findByGermanIdAndRussianId(deutschId,russianId);

        result.ifPresent(value -> assertEquals(deRuPairs, value));
    }

    @Test
    void findAllByDeutschId() {
        Long deutschId=90909090909L;
        DeRuPairs someOne=new DeRuPairs();
        DeRuPairs anyOne=new DeRuPairs();
        DeRuPairs nextTo=new DeRuPairs();

        List<DeRuPairs> deRuPairsList =new ArrayList<>();
        deRuPairsList.add(someOne);
        deRuPairsList.add(anyOne);
        deRuPairsList.add(nextTo);

        when(deRuPairsRepository.getAllByDeutschId(deutschId)).thenReturn(deRuPairsList);
        List<DeRuPairs> result= deRuPairsService.findAllByDeutschId(deutschId);

        assertFalse(result.isEmpty());
        assertEquals(deRuPairsList, result);
    }

    @Test
    void findAllByRussianId() {
        Long russianId=8080808080808L;
        DeRuPairs someOne=new DeRuPairs();
        DeRuPairs anyOne=new DeRuPairs();
        DeRuPairs nextTo=new DeRuPairs();

        List<DeRuPairs> deRuPairsList =new ArrayList<>();
        deRuPairsList.add(someOne);
        deRuPairsList.add(anyOne);
        deRuPairsList.add(nextTo);

        when(deRuPairsRepository.getAllByRussianId(russianId)).thenReturn(deRuPairsList);
        List<DeRuPairs> result= deRuPairsService.findAllByRussianId(russianId);

        assertFalse(result.isEmpty());
        assertEquals(deRuPairsList, result);
    }

    @Test
    void findPairById() {
        Long deRuId=55555555555L;
        DeRuPairs deRuPairs =new DeRuPairs();
        deRuPairs.setId(deRuId);

        when(deRuPairsRepository.findById(deRuId)).thenReturn(Optional.of(deRuPairs));
        Optional<DeRuPairs> result= deRuPairsService.findPairById(deRuId);

        result.ifPresent(value->assertEquals(deRuPairs, value));
    }
}