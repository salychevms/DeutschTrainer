package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.DeRu;
import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Services.UserDictionaryService;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserDictionaryController {
    private final UsersController usersController;
    private final LanguageController languageController;
    private final UserLanguageController userLanguageController;
    private final UserDictionaryService userDictionaryService;

    public UserDictionaryController(UsersController usersController, LanguageController languageController, UserLanguageController userLanguageController, UserDictionaryService userDictionaryService) {
        this.usersController = usersController;
        this.languageController = languageController;
        this.userLanguageController = userLanguageController;
        this.userDictionaryService = userDictionaryService;
    }

    public void saveNewPair(Long telegramId, String languageIdentifier, DeRu pair) {
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        Optional<Language> language = languageController.getLanguageByIdentifier(languageIdentifier);
        if (language.isPresent() && user.isPresent()) {
            Optional<UserLanguage> userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getId(), language.get().getId());
            userLanguage.ifPresent(value -> userDictionaryService.saveNewPair(value, pair));
        }

    }
}
