package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.TrainerDataBase.Controllers.LanguageController;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.Language;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.LanguageService;
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
class LanguageControllerTest {
    @Mock
    private LanguageService languageService;
    @InjectMocks
    private LanguageController languageController;

    @Test
    void testCreateLanguage() {
        String languageName="name";
        String identifier="identifier";

        when(languageService.findLanguageByIdentifierIgnoreCase(identifier)).thenReturn(Optional.empty());
        when(languageService.findByName(languageName)).thenReturn(Optional.empty());

        languageController.createLanguage(languageName, identifier);

        ArgumentCaptor<Language> languageCaptor=ArgumentCaptor.forClass(Language.class);
        verify(languageService, times(1)).createLanguage(languageCaptor.capture());
        Language capturedLanguage=languageCaptor.getValue();

        assertEquals(languageName, capturedLanguage.getName());
        assertEquals(identifier, capturedLanguage.getIdentifier());
    }

    @Test
    void testGetByIdIfExists() {
        Long languageId=888889999999L;
        Language language=new Language("language");
        language.setId(languageId);

        when(languageService.findById(languageId)).thenReturn(Optional.of(language));
        Optional<Language> result=languageController.getById(languageId);

        result.ifPresent(value-> assertEquals(language, value));
    }

    @Test
    void testGetByIdIfDoesNotExist() {
        Long languageId=888889999999L;

        when(languageService.findById(languageId)).thenReturn(Optional.empty());
        Optional<Language> result=languageController.getById(languageId);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void testGetLanguageByIdentifierIfExists() {
        Long id=555444666322L;
        String identifier="IDENTIFIER";
        Language language=new Language("testName");
        language.setId(id);
        language.setIdentifier(identifier);

        when(languageService.findLanguageByIdentifierIgnoreCase(identifier)).thenReturn(Optional.of(language));
        Optional<Language> result=languageController.getLanguageByIdentifier(identifier);

        result.ifPresent(value->assertEquals(language, value));
    }

    @Test
    void testGetLanguageByIdentifierIfDoesNotExist() {
        String identifier="IDENTIFIER";

        when(languageService.findLanguageByIdentifierIgnoreCase(identifier)).thenReturn(Optional.empty());
        Optional<Language> result=languageController.getLanguageByIdentifier(identifier);

        assertEquals(Optional.empty(), result);
    }

    @Test
    void getAll() {
        Language language=new Language();
        Language nextLanguage=new Language();
        Language someOneLanguage=new Language();
        List<Language> languageList=new ArrayList<>();
        languageList.add(language);
        languageList.add(nextLanguage);
        languageList.add(someOneLanguage);

        when(languageService.findAll()).thenReturn(languageList);
        List<Language> result=languageController.getAll();

        assertFalse(result.isEmpty());
        assertEquals(languageList, result);
    }
}