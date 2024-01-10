package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Services.UserDictionaryService;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDictionaryControllerTest {
    @Mock
    private UserDictionaryService userDictionaryService;

    @Mock
    private LanguageController languageController;

    @Mock
    private UserLanguageController userLanguageController;

    @Mock
    private UsersController usersController;

    @InjectMocks
    private UserDictionaryController userDictionaryController;

    @Test
    void testSaveNewPair() {
        Long telegramId = 12354L;
        Users user = new Users(telegramId, "name", new Date());

        Language language = new Language("langName");
        String languageIdentifier = "DE";
        language.setIdentifier(languageIdentifier);

        UserLanguage userLanguage = new UserLanguage(user, language);

        DeRuPairs pair = new DeRuPairs(new Deutsch("de"), new Russian("ru"));

        UserDictionary userDictionary = new UserDictionary(userLanguage, pair, new Date());

        when(usersController.findUserByTelegramId(telegramId)).thenReturn(Optional.of(user));
        when(languageController.getLanguageByIdentifier(languageIdentifier)).thenReturn(Optional.of(language));
        when(userLanguageController.getByUserIdAndLanguageId(telegramId, language.getId())).thenReturn(Optional.of(userLanguage));
        when(userDictionaryService.saveNewPair(userLanguage, pair)).thenReturn(userDictionary);
        UserDictionary result=userDictionaryController.saveNewPair(telegramId, languageIdentifier,pair);

        assertNotNull(result);
        assertEquals(userLanguage, result.getUserLanguage());
        assertEquals(pair, result.getPair());
    }

    @Test
    void testGetById() {
        Long userDictionaryId=555L;
        UserDictionary userDictionary=new UserDictionary(new UserLanguage(), new DeRuPairs(), new Date());
        userDictionary.setId(userDictionaryId);

        when(userDictionaryService.findById(userDictionaryId)).thenReturn(Optional.of(userDictionary));
        Optional<UserDictionary> result=userDictionaryController.getById(userDictionaryId);

        result.ifPresent(value->assertEquals(userDictionary, value));
    }

    @Test
    void testGetUserDictionaryByPairId() {
        DeRuPairs pair=new DeRuPairs();
        Long pairId=687L;
        pair.setId(pairId);
        UserDictionary userDictionary=new UserDictionary();
        userDictionary.setId(66666L);
        userDictionary.setUserLanguage(new UserLanguage());
        userDictionary.setPair(pair);
        userDictionary.setDateAdded(new Date());

        when(userDictionaryService.getUserDictionaryByPairId(pairId)).thenReturn(Optional.of(userDictionary));
        Optional<UserDictionary> result=userDictionaryController.getUserDictionaryByPairId(pairId);

        result.ifPresent(value->assertEquals(userDictionary, value));
    }

    @Test
    void testGetAll() {
        UserDictionary userPair1=new UserDictionary();
        UserDictionary userPair2=new UserDictionary(new UserLanguage(),new DeRuPairs(), new Date());
        UserDictionary userPair3=new UserDictionary(new UserLanguage(new Users(),new Language()),new DeRuPairs(), new Date());

        List<UserDictionary> userDictionaryList=new ArrayList<>();
        userDictionaryList.add(userPair1);
        userDictionaryList.add(userPair2);
        userDictionaryList.add(userPair3);

        when(userDictionaryService.getAll()).thenReturn(userDictionaryList);
        List<UserDictionary> result=userDictionaryController.getAll();

        assertNotNull(result);
        assertEquals(userDictionaryList, result);
    }
}