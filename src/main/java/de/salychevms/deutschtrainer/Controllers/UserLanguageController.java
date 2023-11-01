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

    public Optional<UserLanguage> getByUserIdAndLanguageId(Long userId, Long languageId){
        return userLanguageRepository.findByUserIdAndLanguageId(userId, languageId);
    }

}
