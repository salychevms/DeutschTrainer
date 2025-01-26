package de.salychevms.deutschtrainer.TrainerDataBase.Services;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Language;
import de.salychevms.deutschtrainer.TrainerDataBase.Repo.LanguageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageService(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Transactional
    public Optional<Language> findLanguageByIdentifierIgnoreCase(String identifier) {
        return languageRepository.findLanguageByIdentifierIgnoreCase(identifier);
    }

    @Transactional
    public Optional<Language> findByName(String name) {
        return languageRepository.findByName(name);
    }

    @Transactional
    public void createLanguage(Language language) {
        languageRepository.save(language);
    }

    @Transactional
    public Optional<Language> findById(Long languageId) {
        return languageRepository.findById(languageId);
    }

    @Transactional
    public List<Language> findAll() {
        return languageRepository.findAll();
    }
}
