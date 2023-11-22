package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.Language;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.UserLanguageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserLanguageService {
    private final UserLanguageRepository userLanguageRepository;

    public UserLanguageService(UserLanguageRepository userLanguageRepository) {
        this.userLanguageRepository = userLanguageRepository;
    }

    @Transactional
    public void createUserLanguage(UserLanguage userLanguage) {
        userLanguageRepository.save(userLanguage);
    }

    @Transactional
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

    @Transactional
    public Optional<UserLanguage> getByUserIdAndLanguageId(Long userId, Long languageId){
        return userLanguageRepository.findByUserIdAndLanguageId(userId, languageId);
    }

    @Transactional
    public Optional<UserLanguage> getById(Long id){
        return userLanguageRepository.findById(id);
    }
}
