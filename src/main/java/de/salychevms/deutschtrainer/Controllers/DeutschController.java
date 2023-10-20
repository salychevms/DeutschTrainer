package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.DeRu;
import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Repo.DeRuRepository;
import de.salychevms.deutschtrainer.Repo.DeutschRepository;
import de.salychevms.deutschtrainer.Repo.RussianRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class DeutschController {
    final DeutschRepository deutschRepository;
    final RussianRepository russianRepository;
    final DeRuRepository deRuRepository;

    public DeutschController(DeutschRepository deutschRepository, RussianRepository russianRepository, DeRuRepository deRuRepository) {
        this.deutschRepository = deutschRepository;
        this.russianRepository = russianRepository;
        this.deRuRepository = deRuRepository;
    }

    public Long createNewWord(String data) {
        String[] parts = data.split(" // ");
        Optional<Deutsch> germanRecord = deutschRepository.findByDeWordIgnoreCase(parts[0]);
        if(germanRecord.isEmpty()){
            Deutsch deutsch=new Deutsch(parts[0]);
            return deutschRepository.save(deutsch).getId();
        }else return germanRecord.get().getId();
    }

    public Optional<Deutsch> findById(Long germanId){
        return deutschRepository.findById(germanId);
    }
}
