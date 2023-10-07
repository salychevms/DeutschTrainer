package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.LanguageRepository;
import de.salychevms.deutschtrainer.Repo.UserLanguageRepository;
import de.salychevms.deutschtrainer.Repo.UsersRepository;
import org.springframework.web.bind.annotation.RestController;

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

    public boolean createUserLanguage(Long userId, Long languageId) {
        Optional<Users> user = usersRepository.findById(userId);
        if (user.isPresent()) {
            Optional<Language> language = languageRepository.findById(languageId);
            if (language.isEmpty()) {
                UserLanguage userLanguage = new UserLanguage();
                userLanguage.setUser(user.get());
                userLanguage.setLanguage(language.get());
                userLanguageRepository.save(userLanguage);
                return true;
            } else return false;
        } else return false;
    }

    public Optional<UserLanguage> getUserLanguageByUserIdAndLanguageId(Long userId, Long languageId) {
        return userLanguageRepository.findAllByUserIdAndLanguageId(userId, languageId);
    }

    public List<UserLanguage> getAllUserLanguages() {
        List<UserLanguage> list = userLanguageRepository.findAll();
        if (!list.isEmpty()) {
            return list;
        } else return null;
    }

    public List<UserLanguage> getAllByUserId(Long userId) {
        List<UserLanguage> list = userLanguageRepository.findAllByUserId(userId);
        if (!list.isEmpty()) {
            return list;
        } else return null;
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
