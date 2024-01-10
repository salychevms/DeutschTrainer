package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Services.UserLanguageService;
import org.apache.commons.codec.language.bm.Lang;
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
class UserLanguageControllerTest {
    @Mock
    private UsersController usersController;
    @Mock
    private LanguageController languageController;
    @Mock
    private UserLanguageService userLanguageService;
    @InjectMocks
    private UserLanguageController userLanguageController;

    @Test
    void testCreateUserLanguage() {
        Long telegramId=546189416453L;
        Long languageId=3211541L;
        Users user=new Users(telegramId, "usrName", new Date());
        Language language=new Language("language");
        language.setId(languageId);

        when(usersController.findUserByTelegramId(telegramId)).thenReturn(Optional.of(user));
        when(languageController.getById(languageId)).thenReturn(Optional.of(language));

        userLanguageController.createUserLanguage(telegramId, languageId);
        ArgumentCaptor<UserLanguage> userLanguageCaptor=ArgumentCaptor.forClass(UserLanguage.class);

        verify(userLanguageService, times(1)).createUserLanguage(userLanguageCaptor.capture());
        UserLanguage capturedUserLanguage=userLanguageCaptor.getValue();

        assertEquals(user, capturedUserLanguage.getUser());
        assertEquals(language, capturedUserLanguage.getLanguage());
    }

    @Test
    void testGetAllLanguageIdentifiersByTelegramIdIfExists() {
        Long telegramId=5565456465465L;
        String ident1="ident1";
        String ident2="ident2";
        String ident3="ident3";

        List<String> languageIdentifiers=new ArrayList<>();
        languageIdentifiers.add(ident1);
        languageIdentifiers.add(ident2);
        languageIdentifiers.add(ident3);

        when(userLanguageService.getAllLanguagesByTelegramId(telegramId)).thenReturn(languageIdentifiers);
        List<String> result=userLanguageController.getAllLanguageIdentifiersByTelegramId(telegramId);

        assertEquals(languageIdentifiers, result);
    }

    @Test
    void testGetAllLanguageIdentifiersByTelegramIdIfDoesNotExist() {
        Long telegramId=5565456465465L;

        when(userLanguageService.getAllLanguagesByTelegramId(telegramId)).thenReturn(Collections.emptyList());
        List<String> result=userLanguageController.getAllLanguageIdentifiersByTelegramId(telegramId);

        assertEquals(1, result.size());
        assertEquals("no one yet.", result.get(0));
    }

    @Test
    void testGetByUserIdAndLanguageIdIfExists() {
        Long telegramId=897351L;
        Long languageId=3223L;
        Long userLanguageId=123123L;
        Users user=new Users(telegramId, "userName", new Date());
        Language language=new Language("language");
        language.setId(languageId);
        UserLanguage userLanguage=new UserLanguage(user, language);
        userLanguage.setId(userLanguageId);

        when(usersController.findUserByTelegramId(telegramId)).thenReturn(Optional.of(user));
        when(userLanguageService.getByUserIdAndLanguageId(user, languageId)).thenReturn(Optional.of(userLanguage));
        Optional<UserLanguage> result=userLanguageController.getByUserIdAndLanguageId(telegramId,languageId);

        result.ifPresent(value->assertEquals(userLanguage, value));
    }

    @Test
    void testGetByUserIdAndLanguageIdIfDoesNotExist() {
        Long telegramId=897351L;
        Long languageId=3223L;

        when(usersController.findUserByTelegramId(telegramId)).thenReturn(Optional.empty());
        Optional<UserLanguage> result=userLanguageController.getByUserIdAndLanguageId(telegramId,languageId);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testGetByIdIfExists() {
        Long userLanguageId=11111L;
        UserLanguage userLanguage=new UserLanguage();
        userLanguage.setId(userLanguageId);

        when(userLanguageService.getById(userLanguageId)).thenReturn(Optional.of(userLanguage));
        Optional<UserLanguage> result=userLanguageController.getById(userLanguageId);

        result.ifPresent(value->assertEquals(userLanguage, value));
    }

    @Test
    void testGetByIdIfDoesNotExist() {
        Long userLanguageId=11111L;

        when(userLanguageService.getById(userLanguageId)).thenReturn(Optional.empty());
        Optional<UserLanguage> result=userLanguageController.getById(userLanguageId);

        assertEquals(Optional.empty(), result);
    }
}