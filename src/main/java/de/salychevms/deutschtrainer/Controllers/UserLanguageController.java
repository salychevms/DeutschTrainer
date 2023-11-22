package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Services.UserLanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserLanguageController {
    final UserLanguageService userLanguageService;
    final UsersController usersController;
    final LanguageController languageController;

    @Autowired
    public UserLanguageController(UserLanguageService userLanguageService, UsersController usersController, LanguageController languageController) {
        this.userLanguageService = userLanguageService;
        this.usersController = usersController;
        this.languageController = languageController;
    }

    public void createUserLanguage(Long telegramId, Long languageId) {
        Optional<Users> user = (usersController.findUserByTelegramId(telegramId));
        Optional<Language> language = languageController.getById(languageId);
        UserLanguage userLanguage = new UserLanguage();
        userLanguage.setUser(user.get());
        userLanguage.setLanguage(language.get());
        userLanguageService.createUserLanguage(userLanguage);
    }

    public List<String> getAllLanguagesByTelegramId(Long telegramId) {
        List<String> languages = userLanguageService.getAllLanguagesByTelegramId(telegramId);
        List<String> identifiers = new ArrayList<>(languages);
        if (identifiers.isEmpty()) {
            identifiers.add("no one yet.");
        }
        return identifiers;
    }

    public Optional<UserLanguage> getByUserIdAndLanguageId(Long userId, Long languageId){
        return userLanguageService.getByUserIdAndLanguageId(userId, languageId);
    }

    public Optional<UserLanguage> getById(Long id){
        return userLanguageService.getById(id);
    }
}
