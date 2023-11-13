package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.DeRu;
import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Services.DeRuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class DeRuController {
    private final DeRuService deRuService;
    private final DeutschController deutschController;
    private final RussianController russianController;
    final String DE = "DE";
    final String RU = "RU";

    @Autowired
    public DeRuController(DeRuService deRuService, DeutschController deutschController, RussianController russianController) {
        this.deRuService = deRuService;
        this.deutschController = deutschController;
        this.russianController = russianController;
    }

    public List<Long> createPairs(Long german, List<Long> russian) {
        List<Long> pairs = new ArrayList<>();
        for (Long item : russian) {
            pairs.add(deRuService.createNewPairs(german, item));
        }
        return pairs;
    }

    public String getAllWordPairsByPairId(Long germanId, List<Long> pairsId) {
        StringBuilder pairs = new StringBuilder("You added: "
                + deutschController.findById(germanId).getDeWord()
                + "\ntranslation of the word into russian:");
        for (Long item : pairsId) {
            Optional<DeRu> deRu = deRuService.findById(item);
            if (deRu.isPresent()) {
                Long id = deRu.get().getRussianId();
                String russian = russianController.findById(id).getWord();
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
                wordList.add(item.getWord());
            }
        } else return null;
        return wordList;
    }

    public List<String> getTranslations(String chosenWord, String fromLanguage){
        List<String> translationList=new ArrayList<>();
        if(DE.equalsIgnoreCase(fromLanguage)){
            Optional<Deutsch> deutsch=deutschController.findByWord(chosenWord);
            if(deutsch.isPresent()){
                List<DeRu> allPairs=deRuService.findAllByDeutschId(deutsch.get().getId());
                for(DeRu item:allPairs){
                    translationList.add(russianController.findById(item.getRussianId()).getWord());
                }
            }
        } else if (RU.equalsIgnoreCase(fromLanguage)) {
            Optional<Russian> russian=russianController.findByWord(chosenWord);
            if(russian.isPresent()){
                List<DeRu> allPairs=deRuService.findAllByRussianId(russian.get().getId());
                for(DeRu item:allPairs){
                    translationList.add(deutschController.findById(item.getDeutschId()).getDeWord());
                }
            }
        }
        return translationList;
    }
    public Optional<DeRu> getPairByGermanIdAndRussianId(Long germanId, Long russianId){
        return deRuService.findByGermanIdAndRussianId(germanId,russianId);
    }
    public Optional<DeRu> getDeRuById(Long id){
        return deRuService.findPairById(id);
    }
}
