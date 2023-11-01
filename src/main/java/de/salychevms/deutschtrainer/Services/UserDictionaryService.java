package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.*;
import de.salychevms.deutschtrainer.Repo.UserDictionaryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class UserDictionaryService {
    private final UserDictionaryRepository userDictionaryRepository;

    public UserDictionaryService(UserDictionaryRepository userDictionaryRepository) {
        this.userDictionaryRepository = userDictionaryRepository;
    }

    @Transactional
    public void saveNewPair(UserLanguage userLanguage, DeRu pair){
        UserDictionary userDictionary=new UserDictionary();
        userDictionary.setUserLanguage(userLanguage);
        userDictionary.setPair(pair);
        userDictionary.setDateAdded(new Date());
        userDictionaryRepository.save(userDictionary);
    }
}
