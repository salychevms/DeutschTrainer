package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.LanguageController;
import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.UserDictionaryController;
import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.UserLanguageController;
import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.UsersController;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.*;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.UserDictionaryService;
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

    @Test
    void getAllByTelegramId() {
        Long telegramId=156546L;
        Users user=new Users();
        user.setTelegramId(telegramId);

        String languageIdentifier="DE";
        Language language=new Language();
        language.setIdentifier(languageIdentifier);

        UserLanguage userLanguage=new UserLanguage();
        userLanguage.setLanguage(language);
        userLanguage.setUser(user);

        DeRuPairs pair1=new DeRuPairs();
        DeRuPairs pair2=new DeRuPairs();

        UserDictionary dictionary1=new UserDictionary();
        UserDictionary dictionary2=new UserDictionary();
        dictionary1.setUserLanguage(userLanguage);
        dictionary1.setPair(pair1);
        dictionary2.setUserLanguage(userLanguage);
        dictionary2.setPair(pair2);

        List<UserDictionary> userDictionaryList=new ArrayList<>();
        userDictionaryList.add(dictionary1);
        userDictionaryList.add(dictionary2);

        when(languageController.getLanguageByIdentifier(languageIdentifier)).thenReturn(Optional.of(language));
        when(userLanguageController.getByUserIdAndLanguageId(user.getTelegramId(), language.getId())).thenReturn(Optional.of(userLanguage));
        when(userDictionaryService.getAllByUserLanguage(userLanguage)).thenReturn(userDictionaryList);
        List<UserDictionary> result=userDictionaryController.getAllByTelegramId(telegramId);

        assertNotNull(result);
        assertEquals(userDictionaryList, result);
    }

    @Test
    void getUserDictionaryByPairIdAndUserLanguageId() {
        DeRuPairs pair=new DeRuPairs();
        Long pairId=156546L;
        pair.setId(pairId);

        UserLanguage userLanguage=new UserLanguage();
        Long userLanguageId=687L;
        userLanguage.setId(userLanguageId);

        UserDictionary userDictionary=new UserDictionary();
        userDictionary.setId(1234L);
        userDictionary.setPair(pair);
        userDictionary.setUserLanguage(userLanguage);

        when(userDictionaryService.getUserDictionaryByPairIdAndUserLanguageId(pairId, userLanguageId)).thenReturn(Optional.of(userDictionary));
        Optional<UserDictionary> result=userDictionaryController.getUserDictionaryByPairIdAndUserLanguageId(pairId, userLanguageId);

        assertTrue(result.isPresent());
        assertEquals(userDictionary, result.get());
    }

    @Test
    void getAllByUserLanguageId() {
        Long userLanguageId=556684L;
        UserLanguage userLanguage=new UserLanguage();
        userLanguage.setId(userLanguageId);

        UserDictionary userDictionary=new UserDictionary();
        userDictionary.setUserLanguage(userLanguage);
        UserDictionary userDictionary1=new UserDictionary();
        userDictionary1.setUserLanguage(userLanguage);

        List<UserDictionary> userDictionaryList=new ArrayList<>();
        userDictionaryList.add(userDictionary1);
        userDictionaryList.add(userDictionary);

        when(userDictionaryService.getAllByUserLanguageId(userLanguageId)).thenReturn(userDictionaryList);
        List<UserDictionary> result=userDictionaryController.getAllByUserLanguageId(userLanguageId);

        assertNotNull(result);
        assertEquals(userDictionaryList, result);
    }

    @Test
    void getCountPairsForUserAndLanguageIdentifier() {
        Long telegramId=2311L;
        String languageIdentifier="DE";
        int count=4;

        when(userDictionaryService.getCountPairsForUserAndLanguageIdentifier(telegramId,languageIdentifier)).thenReturn(count);
        int result= userDictionaryController.getCountPairsForUserAndLanguageIdentifier(telegramId,languageIdentifier);

        assertEquals(count,result);
    }

    @Test
    void getCountUniqueGermanWordsForTelegramIdAndLanguageIdentifier() {
        Long telegramId=231121313L;
        String languageIdentifier="DE";
        int count=5;

        when(userDictionaryService.getCountUniqueGermanWordsForTelegramIdAndLanguageIdentifier(telegramId,languageIdentifier)).thenReturn(count);
        int result=userDictionaryController.getCountUniqueGermanWordsForTelegramIdAndLanguageIdentifier(telegramId,languageIdentifier);

        assertEquals(count,result);
    }
}