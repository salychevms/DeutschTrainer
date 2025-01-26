package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Deutsch;
import de.salychevms.deutschtrainer.TrainerDataBase.Repo.DeutschRepository;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.DeutschService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class DeutschServiceTest {

    @Mock
    private DeutschRepository deutschRepository;

    @InjectMocks
    private DeutschService deutschService;

    @Test
    void testFindById() {
        Long deutschId = 9846823092L;
        String deWord = "someOneWord";
        Deutsch deutsch = new Deutsch(deWord);
        deutsch.setId(deutschId);

        when(deutschRepository.findById(deutschId)).thenReturn(Optional.of(deutsch));
        Deutsch result = deutschService.findById(deutschId);

        assertEquals(deutsch, result);
    }

    @Test
    void testCreateNewDeutschIfWordDoesNotExist() {
        String deWord = "someOneWord";
        Deutsch deutsch = new Deutsch(deWord);

        when(deutschRepository.findByDeWordIgnoreCase(deWord)).thenReturn(Optional.empty());
        when(deutschRepository.save(any(Deutsch.class))).thenReturn(deutsch);

        Deutsch result = deutschService.createNewDeutsch(deWord);

        assertNotNull(result);
        assertEquals(deWord, result.getDeWord());

        ArgumentCaptor<Deutsch> deutschCaptor = ArgumentCaptor.forClass(Deutsch.class);
        verify(deutschRepository, times(1)).save(deutschCaptor.capture());

        Deutsch savedDeutsch = deutschCaptor.getValue();
        assertNotNull(savedDeutsch);
        assertEquals(deWord, savedDeutsch.getDeWord());
    }

    @Test
    void testCreateNewDeutschIfWordExists() {
        Long deutschId = 9846823092L;
        String deWord = "someOneWord";
        Deutsch deutsch = new Deutsch(deWord);
        deutsch.setId(deutschId);

        when(deutschRepository.findByDeWordIgnoreCase(deWord)).thenReturn(Optional.of(deutsch));
        Deutsch result = deutschService.createNewDeutsch(deWord);

        assertEquals(deutsch.getDeWord(), result.getDeWord());
    }

    @Test
    void testFindWordsContainingIfExists() {
        Deutsch oneDeutsch = new Deutsch("oneDeutsch");
        Deutsch someOneDeutsch = new Deutsch("someOneDeutsch");
        Deutsch nextDeutsch = new Deutsch("nextDeutsch");

        List<Deutsch> deutschList = new ArrayList<>();
        deutschList.add(oneDeutsch);
        deutschList.add(someOneDeutsch);
        deutschList.add(nextDeutsch);

        String iLookFor = "DEUTSCH";

        when(deutschRepository.findAllByDeWordIsContainingIgnoreCase(iLookFor)).thenReturn(deutschList);
        List<Deutsch> result = deutschService.findWordsContaining(iLookFor);

        assertFalse(result.isEmpty());
        assertEquals(deutschList, result);
    }

    @Test
    void testFindWordsContainingIfDoNotExists() {
        String iLookFor = "DEUTSCH";

        when(deutschRepository.findAllByDeWordIsContainingIgnoreCase(iLookFor)).thenReturn(Collections.emptyList());
        List<Deutsch> result = deutschService.findWordsContaining(iLookFor);

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindByWord() {
        Deutsch deutsch = new Deutsch("deutsch");

        String iLookFor = "DEUTSCH";

        when(deutschRepository.findByDeWord(iLookFor)).thenReturn(Optional.of(deutsch));
        Optional<Deutsch> result = deutschService.findByWord(iLookFor);

        result.ifPresent(value -> assertEquals(deutsch, value));
    }

    @Test
    void testGetAll(){
        Deutsch deutsch1=new Deutsch("d1");
        Deutsch deutsch2=new Deutsch("d2");
        Deutsch deutsch3=new Deutsch("some Deutsch");
        List<Deutsch> deutschList = new ArrayList<>();
        deutschList.add(deutsch1);
        deutschList.add(deutsch2);
        deutschList.add(deutsch3);

        when(deutschRepository.getAll()).thenReturn(deutschList);
        List<Deutsch> result = deutschService.getAll();

        assertEquals(deutschList, result);
        assertFalse(result.isEmpty());
    }
}