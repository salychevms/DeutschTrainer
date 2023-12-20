package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Repo.RussianRepository;
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

class RussianServiceTest {
    @Mock
    private RussianRepository russianRepository;

    @InjectMocks
    private RussianService russianService;

    @Test
    void findById() {
        Long russianId=6871616641L;
        Russian russian=new Russian("ruWord");
        russian.setId(russianId);

        when(russianRepository.findById(russianId)).thenReturn(Optional.of(russian));
        Russian result=russianService.findById(russianId);

        assertEquals(russian, result);
    }

    @Test
    void createNewRussian() {
        Long russianId=6871616641L;
        String ruWord="ruWord";
        Russian russian=new Russian(ruWord);
        russian.setId(russianId);

        when(russianRepository.save(any(Russian.class))).thenReturn(russian);
        Russian result=russianService.createNewRussian(ruWord);

        assertEquals(russian, result);
    }

    @Test
    void findWordsContainingIfExists() {
        Russian oneRussian = new Russian("oneRussian");
        Russian someOneRussian = new Russian("someOneRussian");
        Russian nextRussian = new Russian("nextRussian");

        List<Russian> russianList = new ArrayList<>();
        russianList.add(oneRussian);
        russianList.add(someOneRussian);
        russianList.add(someOneRussian);

        String iLookFor = "RUSSIAN";

        when(russianRepository.findByRuWordContainingIgnoreCase(iLookFor)).thenReturn(russianList);
        List<Russian> result = russianService.findWordsContaining(iLookFor);

        assertFalse(result.isEmpty());
        assertEquals(russianList, result);
    }

    @Test
    void findWordsContainingIfDoNotExist(){
        String iLookFor = "RUSSIAN";

        when(russianRepository.findByRuWordContainingIgnoreCase(iLookFor)).thenReturn(Collections.emptyList());
        List<Russian> result = russianService.findWordsContaining(iLookFor);

        assertTrue(result.isEmpty());
    }

    @Test
    void findByWord() {
        String russianWord="russianWord";
        Russian russian=new Russian(russianWord);

        when(russianRepository.findByRuWord(russianWord)).thenReturn(Optional.of(russian));
        Optional<Russian> result=russianService.findByWord(russianWord);

        result.ifPresent(value -> assertEquals(russian, value));
    }

    @Test
    void get3RandomRussian() {
        String firstRandomWord="firstRandomWord";
        String secondRandomWord="secondRandomWord";
        String thirdRandomWord="thirdRandomWord";

        Russian firstRandomRussian=new Russian(firstRandomWord);
        Russian secondRandomRussian=new Russian(secondRandomWord);
        Russian thirdRandomRussian=new Russian(thirdRandomWord);

        List<Russian> randomList=new ArrayList<>();
        randomList.add(firstRandomRussian);
        randomList.add(secondRandomRussian);
        randomList.add(thirdRandomRussian);

        when(russianRepository.find3RandomRussian()).thenReturn(randomList);
        List<Russian> result=russianService.get3RandomRussian();

        assertEquals(randomList, result);
    }

    @Test
    void get1RandomRussian() {
        String randomWord="randomWord";
        Russian randomRussian=new Russian(randomWord);

        when(russianRepository.find1RandomRussian()).thenReturn(Optional.of(randomRussian));
        Optional<Russian> result=russianService.get1RandomRussian();

        result.ifPresent(value -> assertEquals(randomRussian, value));
    }
}