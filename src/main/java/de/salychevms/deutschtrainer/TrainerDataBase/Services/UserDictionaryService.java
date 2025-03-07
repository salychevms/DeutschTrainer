package de.salychevms.deutschtrainer.TrainerDataBase.Services;

import de.salychevms.deutschtrainer.TrainerDataBase.Repo.UserDictionaryRepository;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.DeRuPairs;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserDictionary;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserLanguage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserDictionaryService {
    private final UserDictionaryRepository userDictionaryRepository;

    public UserDictionaryService(UserDictionaryRepository userDictionaryRepository) {
        this.userDictionaryRepository = userDictionaryRepository;
    }

    @Transactional
    public UserDictionary saveNewPair(UserLanguage userLanguage, DeRuPairs pair) {
        UserDictionary userDictionary = new UserDictionary();
        userDictionary.setUserLanguage(userLanguage);
        userDictionary.setPair(pair);
        userDictionary.setDateAdded(new Date());
        return userDictionaryRepository.save(userDictionary);
    }

    @Transactional
    public Optional<UserDictionary> findById(Long id) {
        return userDictionaryRepository.findById(id);
    }

    @Transactional
    public Optional<UserDictionary> getUserDictionaryByPairId(Long id) {
        return userDictionaryRepository.getUserDictionaryByPairId(id);
    }

    @Transactional
    public List<UserDictionary> getAll() {
        return userDictionaryRepository.findAll();
    }

    @Transactional
    public List<UserDictionary> getAllByUserLanguage(UserLanguage userLanguage) {
        return userDictionaryRepository.getAllByUserLanguage(userLanguage);
    }

    @Transactional
    public Optional<UserDictionary> getUserDictionaryByPairIdAndUserLanguageId(Long pairId, Long userLanguageId) {
        return userDictionaryRepository.getUserDictionaryByPairIdAndUserLanguageId(pairId, userLanguageId);
    }

    @Transactional
    public List<UserDictionary> getAllByUserLanguageId(Long userLanguageId) {
        List<UserDictionary> userDictionaryList = userDictionaryRepository.findAllByUserLanguageId(userLanguageId);
        if (!userDictionaryList.isEmpty()) {
            return userDictionaryList;
        }
        return null;
    }

    @Transactional
    public int getCountPairsForUserAndLanguageIdentifier(Long telegramId, String languageIdentifier) {
        return Math.toIntExact(userDictionaryRepository.countPairsForTelegramIdAndLanguageIdentifier(telegramId, languageIdentifier));
    }

    @Transactional
    public int getCountUniqueGermanWordsForTelegramIdAndLanguageIdentifier(Long telegramId, String languageIdentifier) {
        return Math.toIntExact(userDictionaryRepository.countUniqueGermanWordsForTelegramIdAndLanguageIdentifier(telegramId, languageIdentifier));
    }
}
