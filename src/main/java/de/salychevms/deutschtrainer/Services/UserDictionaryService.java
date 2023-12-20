package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Repo.UserDictionaryRepository;
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
    public UserDictionary saveNewPair(UserLanguage userLanguage, DeRuPairs pair){
        UserDictionary userDictionary=new UserDictionary();
        userDictionary.setUserLanguage(userLanguage);
        userDictionary.setPair(pair);
        userDictionary.setDateAdded(new Date());
        return userDictionaryRepository.save(userDictionary);
    }

    @Transactional
    public Optional<UserDictionary> findById(Long id){
        return userDictionaryRepository.findById(id);
    }

    @Transactional
    public Optional<UserDictionary> getUserDictionaryByPairId(Long id){
        return userDictionaryRepository.getUserDictionaryByPairId(id);
    }

    @Transactional
    public List<UserDictionary> getAll() {
        return userDictionaryRepository.findAll();
    }
}
