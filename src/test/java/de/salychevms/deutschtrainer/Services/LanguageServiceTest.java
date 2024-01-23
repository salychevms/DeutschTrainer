package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Repo.LanguageRepository;
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
class LanguageServiceTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageService languageService;

    @Test
    void findLanguageByIdentifierIgnoreCase() {
        Long languageId=123265464L;
        String languageName="languageName";
        String identifier="IDENTIFIER";
        Language language=new Language(languageName);
        language.setId(languageId);
        language.setIdentifier(identifier);

        when(languageRepository.findLanguageByIdentifierIgnoreCase(identifier)).thenReturn(Optional.of(language));
        Optional<Language> result=languageService.findLanguageByIdentifierIgnoreCase(identifier);

        result.ifPresent(value -> assertEquals(language, value));
    }

    @Test
    void findByName() {
        Long languageId=123265464L;
        String languageName="languageName";
        Language language=new Language(languageName);
        language.setId(languageId);

        when(languageRepository.findByName(languageName)).thenReturn(Optional.of(language));
        Optional<Language> result=languageService.findByName(languageName);

        result.ifPresent(value -> assertEquals(language, value));
    }

    @Test
    void createLanguage() {
        Long languageId=123265464L;
        String languageName="languageName";
        Language language=new Language(languageName);
        language.setId(languageId);

        languageService.createLanguage(language);
        verify(languageRepository, times(1)).save(argThat(newLanguage ->
                newLanguage.getId().equals(languageId) &&
                newLanguage.getName().equals(languageName)));
        ArgumentCaptor<Language> languageCaptor = ArgumentCaptor.forClass(Language.class);

        verify(languageRepository, times(1)).save(languageCaptor.capture());
        Language capturedLanguage=languageCaptor.getValue();

        assertEquals(language, capturedLanguage);
    }

    @Test
    void getById() {
        Long languageId=123265464L;
        String languageName="languageName";
        Language language=new Language(languageName);
        language.setId(languageId);

        when(languageRepository.findById(languageId)).thenReturn(Optional.of(language));
        Optional<Language> result=languageService.findById(languageId);

        result.ifPresent(value -> assertEquals(language, value));
    }

    @Test
    void findAll() {
        List<Language> languageList=new ArrayList<>();
        languageList.add(new Language());
        languageList.add(new Language("someOneName"));
        languageList.add(new Language("newOneName"));

        when(languageRepository.findAll()).thenReturn(languageList);
        List<Language> result=languageService.findAll();

        assertNotNull(result);
        assertEquals(languageList, result);
    }
}