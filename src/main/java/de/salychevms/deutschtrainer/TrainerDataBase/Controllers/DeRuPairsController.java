package de.salychevms.deutschtrainer.TrainerDataBase.Controllers;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.DeRuPairs;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.Deutsch;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.Russian;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserDictionary;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.DeRuPairsService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DeRuPairsController {
    private final DeRuPairsService deRuPairsService;
    private final DeutschController deutschController;
    private final RussianController russianController;
    private final UserDictionaryController userDictionaryController;
    final String DE = "DE";
    final String RU = "RU";

    public DeRuPairsController(DeRuPairsService deRuPairsService, DeutschController deutschController, RussianController russianController, UserDictionaryController userDictionaryController) {
        this.deRuPairsService = deRuPairsService;
        this.deutschController = deutschController;
        this.russianController = russianController;
        this.userDictionaryController = userDictionaryController;
    }

    public List<Long> createPairs(Deutsch german, List<Russian> russian) {
        List<Long> pairs = new ArrayList<>();
        for (Russian item : russian) {
            pairs.add(deRuPairsService.createNewPairs(german, item));
        }
        return pairs;
    }

    public String getAllWordPairsByPairId(Long germanId, List<Long> pairsId) {
        StringBuilder pairs = new StringBuilder("Вы добавили перевод слова: "
                + deutschController.findById(germanId).getDeWord()
                + "\nНа русском это будет:");
        for (Long item : pairsId) {
            Optional<DeRuPairs> deRu = deRuPairsService.findById(item);
            if (deRu.isPresent()) {
                Long id = deRu.get().getRussian().getId();
                String russian = russianController.findById(id).getRuWord();
                pairs.append("\n\t").append(russian);
            }
        }
        return pairs.toString();
    }

    public boolean isItRussian(String russian) {
        final String AbcRU = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        Pattern pattern = Pattern.compile("[" + AbcRU + "]");
        Matcher matcher = pattern.matcher(russian);
        return matcher.find();
    }

    public boolean isItGerman(String german) {
        final String AbcGE = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜßabcdefghijklmnopqrstuvwxyzäöü";
        Pattern pattern = Pattern.compile("[" + AbcGE + "]");
        Matcher matcher = pattern.matcher(german);
        return matcher.find();
    }

    public List<String> getWordsWhichUserLooksFor(Long telegramId, String userWord, String languageIdentifier) {
        List<UserDictionary> userDictionaries = userDictionaryController.getAllByTelegramId(telegramId);
        List<DeRuPairs> pairs = new ArrayList<>();
        for (UserDictionary item : userDictionaries) {
            Optional<DeRuPairs> pair = deRuPairsService.findPairById(item.getPair().getId());
            pair.ifPresent(pairs::add);
        }
        List<String> wordList = new ArrayList<>();
        if (DE.equals(languageIdentifier)) {
            List<Deutsch> german = deutschController.findSmartMatches(userWord);
            for (Deutsch item : german) {
                StringBuilder str = new StringBuilder(item.getDeWord());
                boolean postfixAdded = false;
                for (DeRuPairs someItem : pairs) {
                    if (someItem.getDeutsch().getId().equals(item.getId())) {
                        if (!postfixAdded) {
                            str.append(" <<еще варианты>>");
                        }
                    }
                }
                wordList.add(str.toString());
            }
        } else if (RU.equals(languageIdentifier)) {
            List<Russian> russian = russianController.findSmartMatches(userWord);
            for (Russian item : russian) {
                StringBuilder str = new StringBuilder(item.getRuWord());
                boolean postfixAdded = false;
                for (DeRuPairs someItem : pairs) {
                    if (someItem.getRussian().getId().equals(item.getId())) {
                        if (!postfixAdded) {
                            str.append(" <<еще варианты>>");
                        }
                    }
                }
                wordList.add(str.toString());
            }
        } else return null;
        return wordList;
    }

    public Map<Long, String> getWordMapWhichUserLooksFor(Long telegramId, String userWord, String languageIdentifier) {
        List<UserDictionary> userDictionaries = userDictionaryController.getAllByTelegramId(telegramId);
        List<DeRuPairs> pairs = new ArrayList<>();
        for (UserDictionary item : userDictionaries) {
            Optional<DeRuPairs> pair = deRuPairsService.findPairById(item.getPair().getId());
            pair.ifPresent(pairs::add);
        }
        Map<Long, String> wordMap = new LinkedHashMap<>();
        if (DE.equals(languageIdentifier)) {
            List<Deutsch> german = deutschController.findSmartMatches(userWord);
            for (Deutsch item : german) {
                StringBuilder str = new StringBuilder(item.getDeWord());
                boolean postfixAdded = false;
                for (DeRuPairs someItem : pairs) {
                    if (someItem.getDeutsch().getId().equals(item.getId())) {
                        if (!postfixAdded) { // Добавляем постфикс только если он еще не был добавлен
                            str.append(" <<еще варианты>>");
                            postfixAdded = true;
                        }
                    }
                }
                wordMap.put(item.getId(), str.toString());
            }
        } else if (RU.equals(languageIdentifier)) {
            List<Russian> russian = russianController.findSmartMatches(userWord);
            for (Russian item : russian) {
                StringBuilder str = new StringBuilder(item.getRuWord());
                boolean postfixAdded = false;
                for (DeRuPairs someItem : pairs) {
                    if (someItem.getRussian().getId().equals(item.getId())) {
                        if (!postfixAdded) { // Добавляем постфикс только если он еще не был добавлен
                            str.append(" <<еще варианты>>");
                            postfixAdded = true;
                        }
                    }
                }
                wordMap.put(item.getId(), str.toString());
            }
        } else return null;
        return wordMap;
    }

    public Map<Long, String> getTranslations(Long telegramId, String chosenWord, String fromLanguage) {
        List<UserDictionary> userDictionaries = userDictionaryController.getAllByTelegramId(telegramId);
        List<DeRuPairs> userPairs = new ArrayList<>();
        for (UserDictionary item : userDictionaries) {
            Optional<DeRuPairs> pair = deRuPairsService.findPairById(item.getPair().getId());
            pair.ifPresent(userPairs::add);
        }
        Long chosenWordId = Long.valueOf(chosenWord);
        Map<Long, String> translationList = new HashMap<>();
        if (DE.equalsIgnoreCase(fromLanguage)) {

            List<DeRuPairs> allPairs = deRuPairsService.findAllByDeutschId(chosenWordId);
            for (DeRuPairs item : allPairs) {
                Russian russian = russianController.findById(item.getRussian().getId());
                StringBuilder willSaved = new StringBuilder(russian.getRuWord());
                for (DeRuPairs forCompare : userPairs) {
                    if (item.getRussian().getId().equals(forCompare.getRussian().getId())) {
                        willSaved.append(" <<уже добавлено>>");
                    }
                }
                translationList.put(russian.getId(), willSaved.toString());
            }
        } else if (RU.equalsIgnoreCase(fromLanguage)) {
            List<DeRuPairs> allPairs = deRuPairsService.findAllByRussianId(chosenWordId);
            for (DeRuPairs item : allPairs) {
                Deutsch deutsch = deutschController.findById(item.getDeutsch().getId());
                StringBuilder willSaved = new StringBuilder(deutsch.getDeWord());
                for (DeRuPairs forCompare : userPairs) {
                    if (item.getDeutsch().getId().equals(forCompare.getDeutsch().getId())) {
                        willSaved.append(" <<уже добавлено>>");
                    }
                }
                translationList.put(deutsch.getId(), willSaved.toString());
            }
        }
        return translationList;
    }

    public Optional<DeRuPairs> getPairByGermanIdAndRussianId(Long germanId, Long russianId) {
        return deRuPairsService.findByGermanIdAndRussianId(germanId, russianId);
    }

    public Optional<DeRuPairs> getDeRuById(Long id) {
        return deRuPairsService.findPairById(id);
    }

    public List<DeRuPairs> getAll() {
        return deRuPairsService.getAll();
    }
}
