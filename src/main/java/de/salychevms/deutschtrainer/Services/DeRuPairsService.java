package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.DeRuPairs;
import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Repo.DeRuPairsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeRuPairsService {
    private final DeRuPairsRepository deRuPairsRepository;

    public DeRuPairsService(DeRuPairsRepository deRuPairsRepository) {
        this.deRuPairsRepository = deRuPairsRepository;
    }

    @Transactional
    public Long createNewPairs(Deutsch german, Russian russian) {
        Optional<DeRuPairs> deRu = deRuPairsRepository.getByDeutschIdAndRussianId(german.getId(), russian.getId());
        return deRu.orElseGet(() -> deRuPairsRepository.save(new DeRuPairs(german, russian))).getId();
    }

    @Transactional
    public Optional<DeRuPairs> findById(Long id) {
        return deRuPairsRepository.findById(id);
    }

    @Transactional
    public Optional<DeRuPairs> findByGermanIdAndRussianId(Long germanId, Long russianId) {
        return deRuPairsRepository.getByDeutschIdAndRussianId(germanId, russianId);
    }

    @Transactional
    public List<DeRuPairs> findAllByDeutschId(Long germanId) {
        return deRuPairsRepository.getAllByDeutschId(germanId);
    }

    @Transactional
    public List<DeRuPairs> findAllByRussianId(Long russianId) {
        return deRuPairsRepository.getAllByRussianId(russianId);
    }

    @Transactional
    public Optional<DeRuPairs> findPairById(Long id) {
        return deRuPairsRepository.findById(id);
    }
}
