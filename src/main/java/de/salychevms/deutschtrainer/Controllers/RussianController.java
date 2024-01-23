package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Services.RussianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class RussianController {
    private final RussianService russianService;

    @Autowired
    public RussianController(RussianService russianService) {
        this.russianService = russianService;
    }

    public List<Russian> createNewWords(String data) {
        List<Russian> russianList = new ArrayList<>();
        if(data.equals("<<++++++КОНЕЦ++++++>>")){
            System.out.println("\n\n!!!!!!!!!!!!!!\nThe List is empty or has no more words\n\n");
        }
        if (data.contains(" // ")) {
            String[] parts = data.split(" // ");
            List<String> translations = List.of(parts[1].split("/"));
            for (String item : translations) {
                russianList.add(russianService.createNewRussian(item));
            }
        } else System.out.println("the data has problems there: \n\n" + data);
        if (!russianList.isEmpty()) {
            return russianList;
        } else return null;
    }

    public Russian findById(Long id) {
        Optional<Russian> russian = russianService.findById(id);
        return russian.orElse(null);
    }

    public List<Russian> findAllRussianWordsWhichContain(String russian) {
        return russianService.findWordsContaining(russian);
    }

    public Optional<Russian> findByWord(String word) {
        return russianService.findByWord(word);
    }

    public List<Russian> get3RandomRussian() {
        return russianService.get3RandomRussian();
    }

    public Optional<Russian> get1RandomRussian() {
        return russianService.get1RandomRussian();
    }
}
