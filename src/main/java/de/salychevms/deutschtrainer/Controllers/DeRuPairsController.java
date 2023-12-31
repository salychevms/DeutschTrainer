package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.DeRuPairs;
import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Services.DeRuPairsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class DeRuPairsController {
    private final DeRuPairsService deRuPairsService;
    private final DeutschController deutschController;
    private final RussianController russianController;
    final String DE = "DE";
    final String RU = "RU";

    @Autowired
    public DeRuPairsController(DeRuPairsService deRuPairsService, DeutschController deutschController, RussianController russianController) {
        this.deRuPairsService = deRuPairsService;
        this.deutschController = deutschController;
        this.russianController = russianController;
    }

    public List<Long> createPairs(Deutsch german, List<Russian> russian) {
        List<Long> pairs = new ArrayList<>();
        for (Russian item : russian) {
            pairs.add(deRuPairsService.createNewPairs(german, item));
        }
        return pairs;
    }

    public String getAllWordPairsByPairId(Long germanId, List<Long> pairsId) {
        StringBuilder pairs = new StringBuilder("You added: "
                + deutschController.findById(germanId).getDeWord()
                + "\ntranslation of the word into russian:");
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


    public List<String> getWordsWhichUserLooksFor(String userWord, String language) {
        List<String> wordList = new ArrayList<>();
        if (DE.equals(language)) {
            List<Deutsch> german = deutschController.findAllDeutschWords(userWord);
            for (Deutsch item : german) {
                wordList.add(item.getDeWord());
            }
        } else if (RU.equals(language)) {
            List<Russian> russian = russianController.findAllRussianWords(userWord);
            for (Russian item : russian) {
                wordList.add(item.getRuWord());
            }
        } else return null;
        return wordList;
    }

    public List<String> getTranslations(String chosenWord, String fromLanguage){
        List<String> translationList=new ArrayList<>();
        if(DE.equalsIgnoreCase(fromLanguage)){
            Optional<Deutsch> deutsch=deutschController.findByWord(chosenWord);
            if(deutsch.isPresent()){
                List<DeRuPairs> allPairs= deRuPairsService.findAllByDeutschId(deutsch.get().getId());
                for(DeRuPairs item:allPairs){
                    translationList.add(russianController.findById(item.getRussian().getId()).getRuWord());
                }
            }
        } else if (RU.equalsIgnoreCase(fromLanguage)) {
            Optional<Russian> russian=russianController.findByWord(chosenWord);
            if(russian.isPresent()){
                List<DeRuPairs> allPairs= deRuPairsService.findAllByRussianId(russian.get().getId());
                for(DeRuPairs item:allPairs){
                    translationList.add(deutschController.findById(item.getRussian().getId()).getDeWord());
                }
            }
        }
        return translationList;
    }
    public Optional<DeRuPairs> getPairByGermanIdAndRussianId(Long germanId, Long russianId){
        return deRuPairsService.findByGermanIdAndRussianId(germanId,russianId);
    }
    public Optional<DeRuPairs> getDeRuById(Long id){
        return deRuPairsService.findPairById(id);
    }
}
