package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Services.DeutschService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class DeutschController {
    final DeutschService deutschService;

    @Autowired
    public DeutschController(DeutschService deutschService) {
        this.deutschService = deutschService;
    }

    public Long createNewWord(String data) {
        String[] parts = data.split(" // ");
        return deutschService.createNewDeutsch(parts[0]);
    }

    public Deutsch findById(Long id){
        return deutschService.findById(id);
    }

}
