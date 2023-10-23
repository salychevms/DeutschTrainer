package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Repo.DeutschRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DeutschService {
    private final DeutschRepository deutschRepository;

    public DeutschService(DeutschRepository deutschRepository) {
        this.deutschRepository = deutschRepository;
    }

    @Transactional
    public Deutsch findById(Long id) {
        Optional<Deutsch> deutsch = deutschRepository.findById(id);
        return deutsch.orElse(null);
    }

    @Transactional
    public Long createNewDeutsch(String word) {
        Optional<Deutsch> exist = deutschRepository.findByDeWordIgnoreCase(word);
        if (exist.isEmpty()) {
            return deutschRepository.save(new Deutsch(word)).getId();
        } else return null;
    }
}
