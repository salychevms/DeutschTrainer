package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Repo.LanguageRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class LanguageController {
    final LanguageRepository languageRepository;

    public LanguageController(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    public boolean createLanguage(String name, String identifier) {
        if (languageRepository.findByIdentifier(identifier).isEmpty()
                && languageRepository.findByName(name).isEmpty()) {
            Language language = new Language();
            language.setIdentifier(identifier);
            language.setName(name);
            languageRepository.save(language);
            return true;
        } else return false;
    }

    public Optional<Language> getLanguage(String identifier) {
        if (languageRepository.findByIdentifier(identifier).isPresent()) {
            return languageRepository.findByName(identifier);
        } else return Optional.empty();
    }

    public List<Language> getAll(){
        return languageRepository.findAll();
    }

    public boolean deleteLanguage(String identifier){
        Optional<Language> language=languageRepository.findByIdentifier(identifier);
        if(language.isPresent()){
            languageRepository.deleteById(language.get().getId());
            return true;
        }else return false;
    }

    public boolean deleteAll(){
        if(!languageRepository.findAll().isEmpty()) {
            languageRepository.deleteAll();
            return true;
        }else return false;
    }
}
