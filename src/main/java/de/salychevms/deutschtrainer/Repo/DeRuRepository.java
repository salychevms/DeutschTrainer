package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.DeRu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;


@Repository
public interface DeRuRepository extends JpaRepository<DeRu, Long> {
    Optional<DeRu> getByDeutschIdAndRussianId(Long germanId, Long russianId);

    List<DeRu> getAllByDeutschId(Long id);

    List<DeRu> getAllByRussianId(Long id);
}
