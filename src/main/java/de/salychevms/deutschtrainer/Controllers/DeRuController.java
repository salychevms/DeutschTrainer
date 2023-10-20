package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.DeRu;
import de.salychevms.deutschtrainer.Repo.DeRuRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DeRuController {
    final DeRuRepository deRuRepository;
    final DeutschController deutschController;
    final RussianController russianController;

    public DeRuController(DeRuRepository deRuRepository, DeutschController deutschController, RussianController russianController) {
        this.deRuRepository = deRuRepository;
        this.deutschController = deutschController;
        this.russianController = russianController;
    }

    public List<Long> createNewPairs(Long german, List<Long> russian) {
        List<Long> pairs = new ArrayList<>();
        for (Long item : russian) {
            Optional<DeRu> deRu = deRuRepository.getByDeutschIdAndRussianId(german, item);
            if (deRu.isEmpty()) {
                pairs.add(deRuRepository.save(new DeRu(german, item)).getId());
            }
        }
        return pairs;
    }

    public String getAllWordPairsByPairId(Long germanId, List<Long> pairsId) {
        String pairs = "You added: "
                + deutschController.findById(germanId).get().getDeWord()
                + "\ntranslation of the word into russian:";
        for (Long item : pairsId) {
            Optional<DeRu> deRu = deRuRepository.findById(item);
            if (deRu.isPresent()) {
                Long id = deRu.get().getRussianId();
                String russian = russianController.findById(id).get().getWord();
                pairs = pairs + "\n\t" + russian;
            }
        }
        return pairs;
    }
}
