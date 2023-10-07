package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.MainDictionary;
import de.salychevms.deutschtrainer.Models.UserDictionary;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Repo.MainDictionaryRepository;
import de.salychevms.deutschtrainer.Repo.UserDictionaryRepository;
import de.salychevms.deutschtrainer.Repo.UserLanguageRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class UserDictionaryController {
    final UserDictionaryRepository userDictionaryRepository;
    final UserLanguageRepository userLanguageRepository;
    final MainDictionaryRepository mainDictionaryRepository;

    public UserDictionaryController(UserDictionaryRepository userDictionaryRepository, UserLanguageRepository userLanguageRepository, MainDictionaryRepository mainDictionaryRepository) {
        this.userDictionaryRepository = userDictionaryRepository;
        this.userLanguageRepository = userLanguageRepository;
        this.mainDictionaryRepository = mainDictionaryRepository;
    }

    public boolean addNewWordFromVMainDictionary(Long wordId, Long userLanguageId) {
        Optional<UserLanguage> userLanguage = userLanguageRepository.findById(userLanguageId);
        Optional<MainDictionary> word = mainDictionaryRepository.findById(wordId);
        if (userLanguage.isPresent() && word.isPresent()) {
            if (userDictionaryRepository.findWordByUserLanguageIdAndWordId(userLanguageId, wordId).isEmpty()) {
                UserDictionary userDictionary = new UserDictionary();
                userDictionary.setWord(word.get());
                userDictionary.setUserLanguage(userLanguage.get());
                userDictionary.setAdditionDate(new Date());
                userDictionaryRepository.save(userDictionary);
                return true;
            }else return false;
        }else return false;
    }

    public List<UserDictionary> getAllUsersWords(){
        List<UserDictionary> list=userDictionaryRepository.findAll();
        if(!list.isEmpty()){
            return list;
        }else return null;
    }

    public List<UserDictionary> getAllWordsByUserLanguageId(Long userId){
        List<UserDictionary> list=userDictionaryRepository.findAllWordsByUserLanguageId(userId);
        if(!list.isEmpty()){
            return list;
        }else return null;
    }

    public String getWordByUserDictionaryId(Long userDictionaryId){
        Optional<UserDictionary> UserDictionaryWordId=userDictionaryRepository.findById(userDictionaryId);
        if(UserDictionaryWordId.isPresent()){
            Optional<MainDictionary> MainDictionaryWord=mainDictionaryRepository.findById(UserDictionaryWordId.get().getId());
            return MainDictionaryWord.map(MainDictionary::getWord).orElse(null);
        }return null;
    }

    public boolean deleteCoupleById(Long userDictionaryId){
        Optional<UserDictionary> delete=userDictionaryRepository.findById(userDictionaryId);
        if(userDictionaryId.describeConstable().isPresent()){
            userDictionaryRepository.deleteById(userDictionaryId);
            return true;
        }else return false;
    }

    public boolean deleteAll(){
        List<UserDictionary> deleteAll=userDictionaryRepository.findAll();
        if(!deleteAll.isEmpty()){
            userDictionaryRepository.deleteAll();
            return true;
        }else return false;
    }
}
