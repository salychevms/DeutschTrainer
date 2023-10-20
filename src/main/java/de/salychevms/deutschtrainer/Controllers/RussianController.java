package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Repo.RussianRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RussianController {
    final RussianRepository russianRepository;

    public RussianController(RussianRepository russianRepository) {
        this.russianRepository = russianRepository;
    }

    public List<Long> createNewWord(String data) {
        String[] parts = data.split(" // ");
        List<String> translations = List.of(parts[1].split("/"));
        List<Long> russianList = new ArrayList<>();
        for (String item : translations) {
            Optional<Russian> russian=russianRepository.findByRuWordIgnoreCase(item);
            if (russian.isEmpty()) {
                Russian newRussian = russianRepository.save(new Russian(item));
                russianList.add(newRussian.getId());
            }else russianList.add(russian.get().getId());
        }
        if (!russianList.isEmpty()) {
            return russianList;
        } else return null;
    }

    public Optional<Russian> findById(Long id){
        return russianRepository.findById(id);
    }
}
