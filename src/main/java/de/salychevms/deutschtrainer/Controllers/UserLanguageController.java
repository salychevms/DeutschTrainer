package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.UserLanguageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserLanguageController {
    final UserLanguageRepository userLanguageRepository;
    final UsersController usersController;
    final LanguageController languageController;

    @Autowired
    public UserLanguageController(UserLanguageRepository userLanguageRepository, UsersController usersController, LanguageController languageController) {
        this.userLanguageRepository = userLanguageRepository;

        this.usersController = usersController;
        this.languageController = languageController;
    }

    public void createUserLanguage(Long telegramId, Long languageId) {
        Optional<Users> user = (usersController.findUserByTelegramId(telegramId));
        Optional<Language> language = languageController.getById(languageId);
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
