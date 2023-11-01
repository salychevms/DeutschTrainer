package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Services.RussianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RussianController {
    private final RussianService russianService;

    @Autowired
    public RussianController(RussianService russianService) {
        this.russianService = russianService;
    }

    public List<Long> createNewWords(String data) {
        String[] parts = data.split(" // ");
        List<String> translations = List.of(parts[1].split("/"));
        List<Long> russianList = new ArrayList<>();
        for (String item : translations) {
            russianList.add(russianService.createNewRussian(item));
        }
        if (!russianList.isEmpty()) {
            return russianList;
        } else return null;
    }

    public Russian findById(Long id) {
        return russianService.findById(id);
    }

    public List<Russian> findAllRussianWords(String russian){
        return russianService.findWordsContaining(russian);
    }

    public Optional<Russian> findByWord(String word){
        return russianService.findByWord(word);
    }

}
