package de.salychevms.deutschtrainer.TrainerDataBase.Controllers;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Deutsch;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.DeutschService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
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

    public Optional<Deutsch> findByWord(String word){
        return deutschService.findByWord(word);
    }

    public List<Deutsch> getAll(){
        return deutschService.getAll();
    }

    public List<Deutsch> findSmartMatches(String input) {
        return deutschService.findSmartMatches(input);
    }
}
