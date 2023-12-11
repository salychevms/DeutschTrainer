package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.DeRu;
import de.salychevms.deutschtrainer.Models.Deutsch;
import de.salychevms.deutschtrainer.Models.Russian;
import de.salychevms.deutschtrainer.Repo.DeRuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DeRuService {
    private final DeRuRepository deRuRepository;

    public DeRuService(DeRuRepository deRuRepository) {
        this.deRuRepository = deRuRepository;
    }

    @Transactional
    public Long createNewPairs(Deutsch german, Russian russian) {
        Optional<DeRu> deRu = deRuRepository.getByDeutschIdAndRussianId(german.getId(), russian.getId());
        return deRu.orElseGet(() -> deRuRepository.save(new DeRu(german, russian))).getId();
    }

    @Transactional
    public Optional<DeRu> findById(Long id) {
        return deRuRepository.findById(id);
    }

    @Transactional
    public Optional<DeRu> findByGermanIdAndRussianId(Long germanId, Long russianId){
        return deRuRepository.getByDeutschIdAndRussianId(germanId, russianId);
    }

    @Transactional
    public List<DeRu> findAllByDeutschId(Long germanId) {
        return deRuRepository.getAllByDeutschId(germanId);
    }
    @Transactional
    public List<DeRu> findAllByRussianId(Long russianId) {
        return deRuRepository.getAllByRussianId(russianId);
    }
    @Transactional
    public Optional<DeRu> findPairById(Long id) {
        return deRuRepository.findById(id);
    }
}
