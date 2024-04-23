package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Services.UserDictionaryService;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
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

    public UserDictionary saveNewPair(Long telegramId, String languageIdentifier, DeRuPairs pair) {
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        Optional<Language> language = languageController.getLanguageByIdentifier(languageIdentifier);
        if (language.isPresent() && user.isPresent()) {
            Optional<UserLanguage> userLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getTelegramId(), language.get().getId());
            if (userLanguage.isPresent()) {
                return userDictionaryService.saveNewPair(userLanguage.get(), pair);
            } else {
                userLanguageController.createUserLanguage(user.get().getTelegramId(), language.get().getId());
                Optional<UserLanguage> newUserLanguage = userLanguageController.getByUserIdAndLanguageId(user.get().getTelegramId(), language.get().getId());
                if (newUserLanguage.isPresent()) {
                    return userDictionaryService.saveNewPair(newUserLanguage.get(), pair);
                }
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

    public List<UserDictionary> getAllByTelegramId(Long telegramId) {
        Optional<Language> language = languageController.getLanguageByIdentifier("DE");
        if (language.isPresent()) {
            Optional<UserLanguage> userLanguage = userLanguageController.getByUserIdAndLanguageId(telegramId, language.get().getId());
            if (userLanguage.isPresent()) {
                return userDictionaryService.getAllByUserLanguage(userLanguage.get());
            } else return Collections.emptyList();
        } else return Collections.emptyList();
    }

    public Optional<UserDictionary> getUserDictionaryByPairIdAndUserLanguageId(Long pairId, Long userLanguageId) {
        return userDictionaryService.getUserDictionaryByPairIdAndUserLanguageId(pairId, userLanguageId);
    }

    public List<UserDictionary> getAllByUserLanguageId(Long userLanguageId) {
        return userDictionaryService.getAllByUserLanguageId(userLanguageId);
    }
}
