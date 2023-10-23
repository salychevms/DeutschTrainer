package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.DeRu;
import de.salychevms.deutschtrainer.Repo.DeRuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DeRuService {
    private final DeRuRepository deRuRepository;

    public DeRuService(DeRuRepository deRuRepository) {
        this.deRuRepository = deRuRepository;
    }

    @Transactional
    public Long createNewPairs(Long german, Long russian) {
        Optional<DeRu> deRu = deRuRepository.getByDeutschIdAndRussianId(german, russian);
        return deRu.orElseGet(() -> deRuRepository.save(new DeRu(german, russian))).getId();
    }

    @Transactional
    public Optional<DeRu> findById(Long id) {
        return deRuRepository.findById(id);
    }
}
