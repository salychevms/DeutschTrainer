package de.salychevms.deutschtrainer.DatabaseInit;

import de.salychevms.deutschtrainer.BotConfig.BotConfig;
import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.LanguageRepository;
import de.salychevms.deutschtrainer.Repo.UsersRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final LanguageRepository languageRepository;
    private final UsersRepository usersRepository;
    private final BotConfig config;

    public DatabaseInitializer(LanguageRepository languageRepository, UsersRepository usersRepository, BotConfig config) {
        this.languageRepository = languageRepository;
        this.usersRepository = usersRepository;
        this.config = config;
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
            Users user=new Users(Long.parseLong(config.getId()), "admin", new Date());
            user.setAdmin(true);
            usersRepository.save(user);
        }
    }
}
