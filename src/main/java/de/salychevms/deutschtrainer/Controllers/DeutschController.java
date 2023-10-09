package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Repo.DeutschRepository;
import de.salychevms.deutschtrainer.Repo.RussianRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class DeutschController {
    final DeutschRepository deutschRepository;

    public DeutschController(DeutschRepository deutschRepository) {
        this.deutschRepository = deutschRepository;
    }
}
