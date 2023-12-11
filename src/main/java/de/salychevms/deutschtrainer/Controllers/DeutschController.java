package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Services.DeutschService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class DeutschController {
    private final DeutschService deutschService;

    @Autowired
    public DeutschController(DeutschService deutschService) {
        this.deutschService = deutschService;
    }

    public Deutsch createNewWord(String data) {
        String[] parts = data.split(" // ");
        return deutschService.createNewDeutsch(parts[0]);
    }

    public Deutsch findById(Long id){
        return deutschService.findById(id);
    }

    public List<Deutsch> findAllDeutschWords(String german){
        return deutschService.findWordsContaining(german);
    }

    public Optional<Deutsch> findByWord(String word){
        return deutschService.findByWord(word);
    }

}
