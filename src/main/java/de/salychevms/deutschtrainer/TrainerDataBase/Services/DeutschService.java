package de.salychevms.deutschtrainer.TrainerDataBase.Services;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Deutsch;
import de.salychevms.deutschtrainer.TrainerDataBase.Repo.DeutschRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public Deutsch createNewDeutsch(String word) {
        Optional<Deutsch> exist = deutschRepository.findByDeWordIgnoreCase(word);
        return exist.orElseGet(() -> deutschRepository.save(new Deutsch(word)));
    }

    @Transactional
    public Optional<Deutsch> findByWord(String word){
        return deutschRepository.findByDeWord(word);
    }

    @Transactional
    public List<Deutsch> getAll(){
        return deutschRepository.getAll();
    }

    @Transactional
    public List<Deutsch> findSmartMatches(String input) {
        return deutschRepository.findSmartMatches(input);
    }
}
