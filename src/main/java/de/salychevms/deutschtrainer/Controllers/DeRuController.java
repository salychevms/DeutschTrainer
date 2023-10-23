package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.DeRu;
import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Services.DeRuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
}
