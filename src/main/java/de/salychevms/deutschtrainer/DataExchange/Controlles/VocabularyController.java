package de.salychevms.deutschtrainer.DataExchange.Controlles;

import de.salychevms.deutschtrainer.Controllers.*;
import de.salychevms.deutschtrainer.DataExchange.Classes.BasicPairStatisticInfoClass;
import de.salychevms.deutschtrainer.DataExchange.Classes.BasicVocabularyClass;
import de.salychevms.deutschtrainer.DataExchange.Classes.UserPairStatisticInfoClass;
import de.salychevms.deutschtrainer.DataExchange.Classes.UserVocabularyClass;
import de.salychevms.deutschtrainer.Models.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class VocabularyController {
    private final LanguageController languageController;
    private final UsersController usersController;
    private final UserLanguageController userLanguageController;
    private final UserDictionaryController userDictionaryController;
    private final UserStatisticController userStatisticController;
    private final DeutschController deutschController;
    private final RussianController russianController;
    private final DeRuPairsController deRuPairsController;

    public VocabularyController(LanguageController languageController, UsersController usersController, UserLanguageController userLanguageController, UserDictionaryController userDictionaryController, UserStatisticController userStatisticController, DeutschController deutschController, RussianController russianController, DeRuPairsController deRuPairsController) {
        this.languageController = languageController;
        this.usersController = usersController;
        this.userLanguageController = userLanguageController;
        this.userDictionaryController = userDictionaryController;
        this.userStatisticController = userStatisticController;
        this.deutschController = deutschController;
        this.russianController = russianController;
        this.deRuPairsController = deRuPairsController;
    }

    public List<BasicVocabularyClass> getFullVocabulary() {
        List<DeRuPairs> allPairs = deRuPairsController.getAll();

        List<BasicVocabularyClass> fullVocabulary = new ArrayList<BasicVocabularyClass>();
        for (DeRuPairs pair : allPairs) {
            BasicVocabularyClass temp = new BasicVocabularyClass();
            temp.setPairId(pair.getId());
            temp.setRuId(pair.getRussian().getId());
            temp.setDeId(pair.getDeutsch().getId());
            temp.setRuWord(pair.getRussian().getRuWord());
            temp.setDeWord(pair.getDeutsch().getDeWord());
            fullVocabulary.add(temp);
        }
        return fullVocabulary;
    }

    public List<UserVocabularyClass> getUserVocabularyByTelegramIdAndLanguageId(Long telegramId, Long languageId) {
        List<UserVocabularyClass> userLanguageVocabulary=new ArrayList<>();
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        if (user.isPresent()) {
            Optional<Language> language = languageController.getById(languageId);
            if (language.isPresent()) {
                Optional<UserLanguage> userLanguage = userLanguageController.getByUserIdAndLanguageId(telegramId, languageId);
                if (userLanguage.isPresent()) {
                    List<UserDictionary> userDictionaries = userDictionaryController.getAllByUserLanguageId(languageId);
                    if (!userDictionaries.isEmpty()) {
                        for(UserDictionary userDictionary : userDictionaries) {
                            Optional<DeRuPairs> deRuPair=deRuPairsController.getDeRuById(userDictionary.getId());
                            if (deRuPair.isPresent()){
                                UserVocabularyClass userVocabularyUnit=new UserVocabularyClass();
                                userVocabularyUnit.setTelegramId(telegramId);
                                userVocabularyUnit.setUserDictionaryId(userDictionary.getId());
                                userVocabularyUnit.setDateAdded(userDictionary.getDateAdded());
                                userVocabularyUnit.setPairId(userDictionary.getPair().getId());
                                userVocabularyUnit.setDeId(deRuPair.get().getDeutsch().getId());
                                userVocabularyUnit.setRuId(deRuPair.get().getRussian().getId());
                                userVocabularyUnit.setRuWord(deRuPair.get().getRussian().getRuWord());
                                userVocabularyUnit.setDeWord(deRuPair.get().getDeutsch().getDeWord());
                                userVocabularyUnit.setLanguageId(languageId);
                                userVocabularyUnit.setUserLanguageId(userLanguage.get().getId());
                                userVocabularyUnit.setLanguageIdentifier(language.get().getIdentifier());
                                userLanguageVocabulary.add(userVocabularyUnit);
                            }
                        }
                        return userLanguageVocabulary;
                    }
                }
            }
        }
        return null;
    }

    public List<List<UserVocabularyClass>> getAllUserVocabulariesByTelegramId(Long telegramId){
        List<List<UserVocabularyClass>> allUserVocabularies=new ArrayList<>();
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        if (user.isPresent()) {
            List<Long> userLanguageList=userLanguageController.getLanguageIdsByTelegramId(telegramId);
            if(!userLanguageList.isEmpty()){
                for(Long languageId : userLanguageList) {
                    allUserVocabularies.add(getUserVocabularyByTelegramIdAndLanguageId(telegramId,languageId));
                }
                return allUserVocabularies;
            }
        }
        return null;
    }

    public List<BasicPairStatisticInfoClass> getUserPairStatisticInfo(Long telegramId, Long languageId) {
        List<BasicPairStatisticInfoClass> allPairStatisticInfo=new ArrayList<>();
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        if (user.isPresent()) {
            Optional<UserLanguage> userLanguage=(userLanguageController.getByUserIdAndLanguageId(telegramId, languageId));
            if(userLanguage.isPresent()){
                List<UserDictionary> userDictionaryList=userDictionaryController.getAllByUserLanguageId(userLanguage.get().getId());
                if(!userDictionaryList.isEmpty()){
                    for(UserDictionary userDictionary : userDictionaryList) {
                        Optional<UserStatistic> userStatistic=userStatisticController.getUserStatisticByUserDictionary(userDictionary,userLanguage.get());
                        if(userStatistic.isPresent()){
                            BasicPairStatisticInfoClass pairStatisticInfo=new BasicPairStatisticInfoClass();
                            pairStatisticInfo.setWord(userStatistic.get().getWord().getId());
                            pairStatisticInfo.setStatisticId(userStatistic.get().getId());
                            pairStatisticInfo.setNewWord(userStatistic.get().isNewWord());
                            pairStatisticInfo.setLastTraining(userStatistic.get().getLastTraining());
                            pairStatisticInfo.setIterationsAll(userStatistic.get().getIterationsAll());
                            pairStatisticInfo.setIterationsPerDay(userStatistic.get().getIterationsPerDay());
                            pairStatisticInfo.setIterationsPerWeek(userStatistic.get().getIterationsPerWeek());
                            pairStatisticInfo.setIterationsPerMonth(userStatistic.get().getIterationsPerMonth());
                            pairStatisticInfo.setFailsAll(userStatistic.get().getFailsAll());
                            pairStatisticInfo.setFailsPerDay(userStatistic.get().getFailsPerDay());
                            pairStatisticInfo.setFailsPerWeek(userStatistic.get().getFailsPerWeek());
                            pairStatisticInfo.setFailsPerMonth(userStatistic.get().getFailsPerMonth());
                            allPairStatisticInfo.add(pairStatisticInfo);
                        }
                    }
                    return allPairStatisticInfo;
                }
            }
        }
        return null;
    }

    public List<UserPairStatisticInfoClass> getAllUserPairStatisticInfo(Long telegramId, String identifier) {
        List<UserPairStatisticInfoClass> allPairStatisticInfo=new ArrayList<>();
        Optional<Users> user = usersController.findUserByTelegramId(telegramId);
        if (user.isPresent()) {
            Optional<Language> language = languageController.getLanguageByIdentifier(identifier);
            if (language.isPresent()) {
                Optional<UserLanguage> userLanguage=userLanguageController.getByUserIdAndLanguageId(telegramId, language.get().getId());
                if(userLanguage.isPresent()){
                    List<BasicPairStatisticInfoClass> basicAllPairStatisticInfo=getUserPairStatisticInfo(telegramId,language.get().getId());
                    if(!basicAllPairStatisticInfo.isEmpty()){
                        System.out.println("---------all pairs--------------\n"+basicAllPairStatisticInfo+"\n---------all pairs end list--------------");
                        for(BasicPairStatisticInfoClass pairStatisticInfo : basicAllPairStatisticInfo) {
                            Optional<UserDictionary> userDictionary=userDictionaryController.getUserDictionaryByPairIdAndUserLanguageId(pairStatisticInfo.getWord(), userLanguage.get().getId());
                            if(userDictionary.isPresent()){
                                Optional<DeRuPairs> pair=deRuPairsController.getDeRuById(userDictionary.get().getPair().getId());
                                if(pair.isPresent()){
                                    UserPairStatisticInfoClass userPairStatisticInfo=new UserPairStatisticInfoClass(pairStatisticInfo);
                                    userPairStatisticInfo.setTelegramId(telegramId);
                                    userPairStatisticInfo.setUserLanguageId(userLanguage.get().getId());
                                    userPairStatisticInfo.setUserDictionaryId(userDictionary.get().getId());
                                    userPairStatisticInfo.setPairId(pair.get().getId());
                                    userPairStatisticInfo.setDeId(pair.get().getDeutsch().getId());
                                    userPairStatisticInfo.setRuId(pair.get().getRussian().getId());
                                    userPairStatisticInfo.setRuWord(pair.get().getRussian().getRuWord());
                                    userPairStatisticInfo.setDeWord(pair.get().getDeutsch().getDeWord());
                                    allPairStatisticInfo.add(userPairStatisticInfo);
                                }
                            }
                        }
                    }
                }
            }
        }
        return allPairStatisticInfo;
    }
}
