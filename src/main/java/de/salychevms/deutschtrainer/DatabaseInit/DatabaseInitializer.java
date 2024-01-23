package de.salychevms.deutschtrainer.DatabaseInit;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Repo.LanguageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final LanguageRepository languageRepository;

    public DatabaseInitializer(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public void run(String... args) {
        if(languageRepository.count()==0){
            Language russian=new Language("russian");
            Language german=new Language("german");
            russian.setIdentifier("RU");
            german.setIdentifier("DE");
            languageRepository.save(russian);
            languageRepository.save(german);
        }
    }
}
