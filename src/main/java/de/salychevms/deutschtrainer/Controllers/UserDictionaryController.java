package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Services.UserDictionaryService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

    public UserDictionary saveNewPair(Long telegramId, String languageIdentifier, DeRu pair) {
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        Optional<Language> language = languageController.getLanguageByIdentifier(languageIdentifier);
        if (language.isPresent() && user.isPresent()) {
            Optional<UserLanguage> userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getTelegramId(), language.get().getId());
            if (userLanguage.isPresent()) {
                return userDictionaryService.saveNewPair(userLanguage.get(), pair);
            }
        }
        return null;
    }

    public Optional<UserDictionary> getById(Long userDictionaryId) {
        return userDictionaryService.findById(userDictionaryId);
    }

    public Optional<UserDictionary> getUserDictionaryByPairId(Long deRuId) {
        return userDictionaryService.getUserDictionaryByPairId(deRuId);
    }

    public List<UserDictionary> getAll() {
        return userDictionaryService.getAll();
    }
}
