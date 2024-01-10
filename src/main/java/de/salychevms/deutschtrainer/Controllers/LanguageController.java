package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Services.LanguageService;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Component
public class LanguageController {
    private final LanguageService languageService;

    public LanguageController(LanguageService languageService) {
        this.languageService = languageService;
    }

    public void createLanguage(String name, String identifier) {
        if (languageService.findLanguageByIdentifierIgnoreCase(identifier).isEmpty()
                && languageService.findByName(name).isEmpty()) {
            Language language = new Language();
            language.setIdentifier(identifier);
            language.setName(name);
            languageService.createLanguage(language);
        }
    }

    public Optional<Language> getById(Long languageId) {
        return languageService.findById(languageId);
    }

    public Optional<Language> getLanguageByIdentifier(String identifier) {
        return languageService.findLanguageByIdentifierIgnoreCase(identifier);
    }

    public List<Language> getAll() {
        return languageService.findAll();
    }

}
