package de.salychevms.deutschtrainer.TrainerDataBase.Repo;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.DeRuPairs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface DeRuPairsRepository extends JpaRepository<DeRuPairs, Long> {
    Optional<DeRuPairs> getByDeutschIdAndRussianId(Long germanId, Long russianId);

    List<DeRuPairs> getAllByDeutschId(Long id);

    List<DeRuPairs> getAllByRussianId(Long id);

    @Query("SELECT pair FROM DeRuPairs pair")
    List<DeRuPairs> getAll();
}
