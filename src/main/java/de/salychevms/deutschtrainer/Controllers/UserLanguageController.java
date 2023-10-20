package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.LanguageRepository;
import de.salychevms.deutschtrainer.Repo.UserLanguageRepository;
import de.salychevms.deutschtrainer.Repo.UsersRepository;
import org.hibernate.Hibernate;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserLanguageController {
    final UserLanguageRepository userLanguageRepository;
    final UsersRepository usersRepository;
    final LanguageRepository languageRepository;

    public UserLanguageController(UserLanguageRepository userLanguageRepository, UsersRepository usersRepository, LanguageRepository languageRepository) {
        this.userLanguageRepository = userLanguageRepository;
        this.usersRepository = usersRepository;
        this.languageRepository = languageRepository;
    }

    public void createUserLanguage(Long telegramId, Long languageId) {
        Optional<Users> user = Optional.ofNullable(usersRepository.findByTelegramId(telegramId));
            Optional<Language> language = languageRepository.findById(languageId);
                UserLanguage userLanguage = new UserLanguage();
                userLanguage.setUser(user.get());
                userLanguage.setLanguage(language.get());
                userLanguageRepository.save(userLanguage);
    }

    public List<String> getAllLanguagesByTelegramId(Long telegramId) {
        List<String> identifiers = new ArrayList<>();
        List<UserLanguage> languages = userLanguageRepository.findAllByUser_TelegramId(telegramId);
        for (UserLanguage item : languages) {
            identifiers.add(item.getLanguage().getIdentifier());
        }
        if (identifiers.isEmpty()) {
            identifiers.add("no one yet.");
        }
        return identifiers;
    }

    public List<UserLanguage> getAllByUserId(Long telegramId) {
        return userLanguageRepository.findAllByUser_TelegramId(telegramId);
    }

    public List<UserLanguage> getAllByLanguageId(Long languageId) {
        List<UserLanguage> list = userLanguageRepository.findAllByLanguageId(languageId);
        if (!list.isEmpty()) {
            return list;
        } else return null;
    }

    public List<UserLanguage> getAllByLanguageName(String languageName) {
        List<UserLanguage> list = userLanguageRepository.findAllByLanguageName(languageName);
        if (!list.isEmpty()) {
            return list;
        } else return null;
    }

    public boolean deleteAll() {
        List<UserLanguage> list = userLanguageRepository.findAll();
        if (!list.isEmpty()) {
            userLanguageRepository.deleteAll();
            return true;
        } else return false;
    }

    public boolean deleteUserLanguageById(Long id) {
        Optional<UserLanguage> language = userLanguageRepository.findById(id);
        if (language.isPresent()) {
            userLanguageRepository.deleteById(id);
            return true;
        } else return false;
    }
}
