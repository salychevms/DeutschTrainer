package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Language;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserLanguage;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.Users;
import de.salychevms.deutschtrainer.TrainerDataBase.Repo.UserLanguageRepository;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.UserLanguageService;
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
class UserLanguageServiceTest {
    @Mock
    private UserLanguageRepository userLanguageRepository;

    @InjectMocks
    private UserLanguageService userLanguageService;

    @Test
    void createUserLanguage() {
        Long userId = 12334455L;
        Users user = new Users(userId, "userName", new Date());
        Language language = new Language("testLanguage");
        Long languageId = 5544332211L;
        language.setId(languageId);
        UserLanguage userLanguage = new UserLanguage(user, language);
        Long userLanguageId = 76549876321L;
        userLanguage.setId(userLanguageId);

        userLanguageService.createUserLanguage(userLanguage);
        verify(userLanguageRepository, times(1)).save(argThat(newUserLanguage ->
                newUserLanguage.getId().equals(userLanguageId) &&
                        newUserLanguage.getLanguage().equals(language) &&
                        newUserLanguage.getUser().equals(user)));
        ArgumentCaptor<UserLanguage> userLanguageCaptor = ArgumentCaptor.forClass(UserLanguage.class);

        verify(userLanguageRepository, times(1)).save(userLanguageCaptor.capture());
        UserLanguage capturedUserLanguage = userLanguageCaptor.getValue();

        assertEquals(userLanguage, capturedUserLanguage);
    }

    @Test
    void getAllLanguagesByTelegramIdIfNoOneLanguageExists() {
        Long userId = 666665550000000L;
        when(userLanguageRepository.findAllByUser_TelegramId(userId)).thenReturn(Collections.emptyList());
        List<String> noOneLanguage=userLanguageService.getAllLanguagesByTelegramId(userId);
        assertEquals(1, noOneLanguage.size());
        assertTrue(noOneLanguage.contains("no one yet."));
    }

    @Test
    void getAllLanguagesByTelegramId() {
        Long userId = 44444440000000L;
        String userName = "firstUserName";
        Users user = new Users(userId, userName, new Date());

        Language firstLanguage = new Language("firstLanguage");
        Language secondLanguage = new Language("secondLanguage");
        firstLanguage.setId(999444000L);
        secondLanguage.setId(5678900321L);
        String firstIdentifier="FIRSTLANGUAGEIdentifier";
        String secondIdentifier="SECONDLANGUAGEIdentifier";
        firstLanguage.setIdentifier(firstIdentifier);
        secondLanguage.setIdentifier(secondIdentifier);

        UserLanguage firstUserLanguage = new UserLanguage(user, firstLanguage);
        UserLanguage secondUserLanguage = new UserLanguage(user, secondLanguage);
        List<UserLanguage> userLanguageList = new ArrayList<>();
        userLanguageList.add(firstUserLanguage);
        userLanguageList.add(secondUserLanguage);

        when(userLanguageRepository.findAllByUser_TelegramId(userId)).thenReturn(userLanguageList);

        List<String> expectedIdentifierList = userLanguageService.getAllLanguagesByTelegramId(userId);

        assertEquals(2, expectedIdentifierList.size());
        assertTrue(expectedIdentifierList.contains(firstIdentifier));
        assertTrue(expectedIdentifierList.contains(secondIdentifier));
    }

    @Test
    void getByUserIdAndLanguageId() {
        Long userId=56454654L;
        String userName="userName";
        Users user=new Users(userId, userName, new Date());

        Long languageId=109109170510L;
        String languageName="languageName";
        String identifier="IDENTIFIER";
        Language language=new Language(languageName);
        language.setIdentifier(identifier);
        language.setId(languageId);

        Long userLanguageId=6548711131L;
        UserLanguage userLanguage=new UserLanguage(user, language);
        userLanguage.setId(userLanguageId);

        when(userLanguageRepository.findByUserAndLanguageId(user, languageId)).thenReturn(Optional.of(userLanguage));
        Optional<UserLanguage> result=userLanguageService.getByUserIdAndLanguageId(user, languageId);

        result.ifPresent(value -> assertEquals(userLanguage, value));
    }

    @Test
    void getById() {
        Long userId=73607626072L;
        String userName="userName";
        Users user=new Users(userId, userName, new Date());

        Long languageId=1089976L;
        String languageName="languageName";
        String identifier="IDENTIFIER";
        Language language=new Language(languageName);
        language.setIdentifier(identifier);
        language.setId(languageId);

        Long userLanguageId=15152351131L;
        UserLanguage userLanguage=new UserLanguage(user, language);
        userLanguage.setId(userLanguageId);

        when(userLanguageRepository.findById(userLanguageId)).thenReturn(Optional.of(userLanguage));
        Optional<UserLanguage> result=userLanguageService.getById(userLanguageId);

        result.ifPresent(value -> assertEquals(userLanguage, value));
    }

    @Test
    void testGetAllLanguageIdsByTelegramId(){
        Long telegramId=73607626072L;
        Users user=new Users();
        user.setTelegramId(telegramId);

        Language language1=new Language();
        language1.setIdentifier("language1");

        UserLanguage userLanguage=new UserLanguage();
        userLanguage.setLanguage(language1);
        userLanguage.setUser(user);

        Language language2=new Language();
        language2.setIdentifier("language2");

        UserLanguage someUserLanguage=new UserLanguage();
        someUserLanguage.setLanguage(language2);
        someUserLanguage.setUser(user);

        List<UserLanguage> userLanguageList = new ArrayList<>();
        userLanguageList.add(userLanguage);
        userLanguageList.add(someUserLanguage);

        List<String> languageNames=new ArrayList<>();
        languageNames.add("language1");
        languageNames.add("language2");

        when(userLanguageRepository.findAllByUser_TelegramId(telegramId)).thenReturn(userLanguageList);
        List<String> result=userLanguageService.getAllLanguagesByTelegramId(telegramId);

        assertFalse(result.isEmpty());
        assertEquals(languageNames, result);
    }
}