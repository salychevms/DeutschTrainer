package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.DeRu;
import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Services.DeRuService;
import de.salychevms.deutschtrainer.Services.RussianService;
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
        String pairs = "You added: "
                + deutschController.findById(germanId).getDeWord()
                + "\ntranslation of the word into russian:";
        for (Long item : pairsId) {
            Optional<DeRu> deRu = deRuService.findById(item);
            if (deRu.isPresent()) {
                Long id = deRu.get().getRussianId();
                String russian = russianController.findById(id).getWord();
                pairs = pairs + "\n\t" + russian;
            }
        }
        return pairs;
    }

    public boolean isItRussian(String russian) {
        final String RU = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        Pattern pattern = Pattern.compile("[" + RU + "]");
        Matcher matcher = pattern.matcher(russian);
        return matcher.find();
    }

    public boolean isItGerman(String german) {
        final String GE = "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÜßabcdefghijklmnopqrstuvwxyzäöü";
        Pattern pattern = Pattern.compile("[" + GE + "]");
        Matcher matcher = pattern.matcher(german);
        return matcher.find();
    }

    public List<String> getWordsToUser(String userText) {
        List<String> wordCollection = new ArrayList<>();
        System.out.println(isItGerman(userText)+" - ge\n"+isItRussian(userText)+" - ru");/////////////////////////////////////////////////////////////////////////////////////////////
        if (isItGerman(userText)) {
            List<Deutsch> allGerman = deutschController.findAllDeutschWords(userText);
            System.out.println(allGerman);////////////////////////////////////////////////////////////////////////////////////////
            List<DeRu> allPairsByGermanId = new ArrayList<>();
            for (Deutsch germanItem : allGerman) {
                allPairsByGermanId = deRuService.findAllByDeutschId(germanItem.getId());
                for (DeRu deRuItem : allPairsByGermanId) {
                    wordCollection.add(deutschController.findById(deRuItem.getDeutschId()).getDeWord()
                            + " // "
                            + russianController.findById(deRuItem.getRussianId()).getWord());
                }
            }
        } else if (isItRussian(userText)) {
            List<Russian> allRussian = russianController.findAllRussianWords(userText);
            List<DeRu> allPairsByRussianId = new ArrayList<>();
            for (Russian russianItem : allRussian) {
                allPairsByRussianId = deRuService.findAllByRussianId(russianItem.getId());
                for (DeRu deRuItem : allPairsByRussianId) {
                    wordCollection.add(russianController.findById(deRuItem.getRussianId()).getWord()
                            + " // "
                            + deutschController.findById(deRuItem.getDeutschId()).getDeWord());
                }
            }
        }
        return wordCollection;
    }
}
