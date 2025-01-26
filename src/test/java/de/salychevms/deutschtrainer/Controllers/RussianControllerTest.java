package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.RussianController;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.Russian;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.RussianService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RussianControllerTest {
    @Mock
    private RussianService russianService;

    @InjectMocks
    private RussianController russianController;

    @Test
    void testCreateNewWords() {
        Long ru1Id = 1111111L;
        Long ru2Id = 2222222L;
        String income = "de // w1/w2";
        String w1 = "w1";
        String w2 = "w2";
        Russian ru1 = new Russian(w1);
        Russian ru2 = new Russian(w2);
        ru1.setId(ru1Id);
        ru2.setId(ru2Id);
        List<Russian> russianList = new ArrayList<>();
        russianList.add(ru1);
        russianList.add(ru2);

        when(russianService.createNewRussian(w1)).thenReturn(ru1);
        when(russianService.createNewRussian(w2)).thenReturn(ru2);

        List<Russian> ruResult = russianController.createNewWords(income);

        verify(russianService, times(1)).createNewRussian(w1);
        verify(russianService, times(1)).createNewRussian(w2);

        assertEquals(russianList, ruResult);
        assertEquals(ru1, ruResult.get(0));
        assertEquals(ru2, ruResult.get(1));
    }

    @Test
    void testFindByIdIfExists() {
        Long ruId = 111222333L;
        String word = "word";
        Russian russian = new Russian(word);
        russian.setId(ruId);

        when(russianService.findById(ruId)).thenReturn(Optional.of(russian));
        Russian result = russianController.findById(ruId);

        assertEquals(russian, result);
    }

    @Test
    void testFindByIdIfDoesNotExist() {
        Long ruId = 111222333L;

        when(russianService.findById(ruId)).thenReturn(Optional.empty());
        Russian result = russianController.findById(ruId);

        assertNull(result);
    }

    @Test
    void findAllRussianWordsWhichContainIfContain() {
        String part = "part";
        Russian ru1 = new Russian(part);
        Russian ru2 = new Russian("apart");
        Russian ru3 = new Russian("party");
        List<Russian> russianList = new ArrayList<>();
        russianList.add(ru3);
        russianList.add(ru2);
        russianList.add(ru1);

        when(russianService.findWordsContaining(part)).thenReturn(russianList);
        List<Russian> result = russianController.findAllRussianWordsWhichContain(part);

        assertFalse(result.isEmpty());
        assertEquals(russianList, result);
    }

    @Test
    void findAllRussianWordsWhichContainIfDoesNotContain() {
        String part = "part";

        when(russianService.findWordsContaining(part)).thenReturn(Collections.emptyList());
        List<Russian> result = russianController.findAllRussianWordsWhichContain(part);

        assertTrue(result.isEmpty());
        assertEquals(Collections.emptyList(), result);
    }

    @Test
    void testFindByWordIfExists() {
        String word = "word";
        Long id = 55465464L;
        Russian russian = new Russian(word);
        russian.setId(id);

        when(russianService.findByWord(word)).thenReturn(Optional.of(russian));
        Optional<Russian> result = russianController.findByWord(word);

        result.ifPresent(value -> assertEquals(russian, value));
    }

    @Test
    void testFindByWordIfDoesNotExist() {
        String word = "word";

        when(russianService.findByWord(word)).thenReturn(Optional.empty());
        Optional<Russian> result = russianController.findByWord(word);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testGet3RandomRussian() {
        Russian ru1 = new Russian("random1");
        Russian ru2 = new Russian("random2");
        Russian ru3 = new Russian("random3");
        List<Russian> random3 = new ArrayList<>();
        random3.add(ru1);
        random3.add(ru2);
        random3.add(ru3);

        when(russianService.get3RandomRussian()).thenReturn(random3);
        List<Russian> result = russianController.get3RandomRussian();

        assertFalse(result.isEmpty());
        assertEquals(random3, result);
    }

    @Test
    void get1RandomRussian() {
        Long id=68764354L;
        Russian russian = new Russian("random");
        russian.setId(id);

        when(russianService.get1RandomRussian()).thenReturn(Optional.of(russian));
        Optional<Russian> result=russianController.get1RandomRussian();

        result.ifPresent(value->assertEquals(russian, value));
    }

    @Test
    void testGetAll(){
        Russian russian1=new Russian();
        Russian russian2=new Russian();

        List<Russian> russianList = new ArrayList<>();
        russianList.add(russian1);
        russianList.add(russian2);

        when(russianService.getAll()).thenReturn(russianList);
        List<Russian> result = russianController.getAll();

        assertFalse(result.isEmpty());
        assertEquals(russianList, result);
    }
}