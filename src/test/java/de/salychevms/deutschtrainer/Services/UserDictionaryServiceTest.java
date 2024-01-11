package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.DeRuPairs;
import de.salychevms.deutschtrainer.Models.UserDictionary;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Repo.UserDictionaryRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDictionaryServiceTest {
    @Mock
    private UserDictionaryRepository userDictionaryRepository;

    @InjectMocks
    private UserDictionaryService userDictionaryService;

    @Test
    void saveNewPair() {
        Long userLanguageId=884578548L;
        Long deRuId=66663624664L;
        Long userDictionaryId=2233344555L;
        UserLanguage userLanguage=new UserLanguage();
        DeRuPairs deRuPairs =new DeRuPairs();
        userLanguage.setId(userLanguageId);
        deRuPairs.setId(deRuId);
        UserDictionary userDictionary=new UserDictionary(userLanguage, deRuPairs, new Date());
        userDictionary.setId(userDictionaryId);

        when(userDictionaryRepository.save(any(UserDictionary.class))).thenReturn(userDictionary);
        UserDictionary result=userDictionaryService.saveNewPair(userLanguage, deRuPairs);
        ArgumentCaptor<UserDictionary> userDictionaryCaptor=ArgumentCaptor.forClass(UserDictionary.class);

        verify(userDictionaryRepository, times(1)).save(userDictionaryCaptor.capture());
        UserDictionary capturedUserDictionary=userDictionaryCaptor.getValue();
        capturedUserDictionary.setId(userDictionaryId);

        assertEquals(result.getId(), capturedUserDictionary.getId());
        assertEquals(result.getPair(), capturedUserDictionary.getPair());
        assertEquals(result.getUserLanguage(), capturedUserDictionary.getUserLanguage());
    }

    @Test
    void findById() {
        UserLanguage userLanguage=new UserLanguage();
        DeRuPairs deRuPairs =new DeRuPairs();
        UserDictionary userDictionary=new UserDictionary(userLanguage, deRuPairs, new Date());
        Long userDictionaryId=666555447778899L;
        userDictionary.setId(userDictionaryId);

        when(userDictionaryRepository.findById(userDictionaryId)).thenReturn(Optional.of(userDictionary));
        Optional<UserDictionary> result=userDictionaryService.findById(userDictionaryId);

        result.ifPresent(value->assertEquals(userDictionary, value));
    }

    @Test
    void getUserDictionaryByPairId() {
        UserLanguage userLanguage=new UserLanguage();
        DeRuPairs pair=new DeRuPairs();
        Long pairId=8273568273568723L;
        pair.setId(pairId);
        UserDictionary userDictionary=new UserDictionary(userLanguage, pair, new Date());

        when(userDictionaryRepository.getUserDictionaryByPairId(pairId)).thenReturn(Optional.of(userDictionary));
        Optional<UserDictionary> result=userDictionaryService.getUserDictionaryByPairId(pairId);

        result.ifPresent(value->assertEquals(userDictionary, value));
    }

    @Test
    void getAll() {
        UserDictionary firstUserDictionary=new UserDictionary();
        UserDictionary secondUserDictionary=new UserDictionary();
        UserDictionary thirdUserDictionary=new UserDictionary();
        List<UserDictionary> userDictionaries=new ArrayList<>();

        userDictionaries.add(firstUserDictionary);
        userDictionaries.add(secondUserDictionary);
        userDictionaries.add(thirdUserDictionary);

        when(userDictionaryRepository.findAll()).thenReturn(userDictionaries);
        List<UserDictionary> result=userDictionaryService.getAll();

        assertFalse(result.isEmpty());
        assertEquals(userDictionaries, result);
    }

    @Test
    void testGetUserDictionaryByUserLanguage(){
        UserLanguage userLanguage=new UserLanguage();
        UserDictionary userDictionary=new UserDictionary(userLanguage, new DeRuPairs(),new Date());

        when(userDictionaryRepository.getByUserLanguage(userLanguage)).thenReturn(Optional.of(userDictionary));
        Optional<UserDictionary> result=userDictionaryService.getByUserLanguage(userLanguage);

        result.ifPresent(value->assertEquals(userDictionary, value));
    }
}