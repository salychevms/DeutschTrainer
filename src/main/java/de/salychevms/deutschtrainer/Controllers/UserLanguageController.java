package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.UserDictionary;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Services.UserDictionaryService;
import de.salychevms.deutschtrainer.Services.UserLanguageService;
import org.bouncycastle.pqc.crypto.newhope.NHSecretKeyProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UserLanguageController {
    final UserLanguageService userLanguageService;
    final UsersController usersController;
    final LanguageController languageController;
    private final UserDictionaryService userDictionaryService;

    @Autowired
    public UserLanguageController(UserLanguageService userLanguageService, UsersController usersController, LanguageController languageController, UserDictionaryService userDictionaryService) {
        this.userLanguageService = userLanguageService;
        this.usersController = usersController;
        this.languageController = languageController;
        this.userDictionaryService = userDictionaryService;
    }

    public void createUserLanguage(Long telegramId, Long languageId) {
        Optional<Users> user = (usersController.findUserByTelegramId(telegramId));
        if (user.isPresent()) {
            Optional<Language> language = languageController.getById(languageId);
            if (language.isPresent()) {
                UserLanguage userLanguage = new UserLanguage();
                userLanguage.setUser(user.get());
                userLanguage.setLanguage(language.get());
                userLanguageService.createUserLanguage(userLanguage);
            }
        }
    }

    public List<String> getAllLanguageIdentifiersByTelegramId(Long telegramId) {
        List<String> languages = userLanguageService.getAllLanguagesByTelegramId(telegramId);
        List<String> identifiers = new ArrayList<>(languages);
        if (identifiers.isEmpty()) {
            identifiers.add("no one yet.");
        }
        return identifiers;
    }

    public Optional<UserLanguage> getByUserIdAndLanguageId(Long telegramId, Long languageId) {
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        if (user.isPresent()) {
            return userLanguageService.getByUserIdAndLanguageId(user.get(), languageId);
        } else {
            return Optional.empty();
        }
    }

    public Optional<UserLanguage> getById(Long id) {
        return userLanguageService.getById(id);
    }

    public List<Long> getLanguageIdsByTelegramId(Long telegramId) {
        return userLanguageService.getAllLanguageIdsByTelegramId(telegramId);
    }
}
