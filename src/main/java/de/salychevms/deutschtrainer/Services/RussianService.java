package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Repo.RussianRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RussianService {
    private final RussianRepository russianRepository;

    public RussianService(RussianRepository russianRepository) {
        this.russianRepository = russianRepository;
    }

    @Transactional
    public Optional<Russian> findById(Long id){
        return russianRepository.findById(id);
    }

    @Transactional
    public Russian createNewRussian(String word){
        Optional<Russian> exist=russianRepository.findByRuWordIgnoreCase(word);
        return exist.orElseGet(() -> russianRepository.save(new Russian(word)));
    }

    @Transactional
    public List<Russian> findWordsContaining(String russian){
        return russianRepository.findByRuWordContainingIgnoreCase(russian);
    }

    @Transactional
    public Optional<Russian> findByWord(String word){
        return russianRepository.findByRuWord(word);
    }

    @Transactional
    public List<Russian> get3RandomRussian(){
        return russianRepository.find3RandomRussian();
    }

    @Transactional
    public Optional<Russian> get1RandomRussian(){
        return russianRepository.find1RandomRussian();
    }
}
