package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.DeutschController;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.Deutsch;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.DeutschService;
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
class DeutschControllerTest {
    @Mock
    private DeutschService deutschService;

    @InjectMocks
    private DeutschController deutschController;

    @Test
    void testCreateNewWord() {
        Long deutschId=165165151651L;
        String newWord="newWord";
        Deutsch deutsch=new Deutsch(newWord);
        deutsch.setId(deutschId);

        when(deutschService.createNewDeutsch(newWord)).thenReturn(deutsch);
        Deutsch result=deutschController.createNewWord(newWord);

        verify(deutschService, times(1)).createNewDeutsch(newWord);

        assertEquals(deutsch, result);
    }

    @Test
    void testFindById() {
        Long deutschId=55444336668789878L;
        String word="word";
        Deutsch deutsch=new Deutsch(word);
        deutsch.setId(deutschId);

        when(deutschService.findById(deutschId)).thenReturn(deutsch);
        Deutsch result=deutschController.findById(deutschId);

        assertEquals(deutsch, result);
    }

    @Test
    void testFindAllDeutschWordsWhichContainIfContain() {
        String partOfWord="part";
        Deutsch deutsch=new Deutsch("part");
        Deutsch someOneDeutsch=new Deutsch("apart");
        Deutsch nextDeutsch=new Deutsch("party");
        List<Deutsch> deutschList=new ArrayList<>();
        deutschList.add(deutsch);
        deutschList.add(someOneDeutsch);
        deutschList.add(nextDeutsch);

        when(deutschService.findWordsContaining(partOfWord)).thenReturn(deutschList);
        List<Deutsch> result=deutschController.findAllDeutschWordsWhichContain(partOfWord);

        assertFalse(result.isEmpty());
        assertEquals(deutschList, result);
    }

    @Test
    void testFindAllDeutschWordsWhichContainIfDoNotExist() {
        String partOfWord="part";

        when(deutschService.findWordsContaining(partOfWord)).thenReturn(Collections.emptyList());
        List<Deutsch> result=deutschController.findAllDeutschWordsWhichContain(partOfWord);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByWordIfExists() {
        Long id=665489784654351L;
        String word="word";
        Deutsch deutsch=new Deutsch(word);
        deutsch.setId(id);

        when(deutschService.findByWord(word)).thenReturn(Optional.of(deutsch));
        Optional<Deutsch> result=deutschController.findByWord(word);

        result.ifPresent(value->assertEquals(deutsch, value));
    }

    @Test
    void testFindByWordIfDoesNotExist() {
        String word="word";

        when(deutschService.findByWord(word)).thenReturn(Optional.empty());
        Optional<Deutsch> result=deutschController.findByWord(word);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAll(){
        Deutsch deutsch1=new Deutsch();
        Deutsch deutsch2=new Deutsch();

        List<Deutsch> deutschList=new ArrayList<>();
        deutschList.add(deutsch1);

        when(deutschService.getAll()).thenReturn(deutschList);
        List<Deutsch> result=deutschController.getAll();

        assertFalse(result.isEmpty());
        assertEquals(deutschList, result);
    }
}