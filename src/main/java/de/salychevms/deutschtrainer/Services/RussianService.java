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
    public Russian findById(Long id){
        Optional<Russian> russian=russianRepository.findById(id);
        return russian.orElse(null);
    }

    @Transactional
    public Long createNewRussian(String word){
        Optional<Russian> exist=russianRepository.findByRuWordIgnoreCase(word);
        if(exist.isEmpty()){
            return russianRepository.save(new Russian(word)).getId();
        }else return exist.get().getId();
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
