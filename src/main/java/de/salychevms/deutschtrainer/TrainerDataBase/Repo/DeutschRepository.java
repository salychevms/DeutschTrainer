package de.salychevms.deutschtrainer.TrainerDataBase.Repo;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Deutsch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeutschRepository extends JpaRepository<Deutsch, Long> {
    Optional<Deutsch> findByDeWordIgnoreCase(String word);

    Optional<Deutsch> findByDeWord(String word);

    List<Deutsch> findAllByDeWordIsContainingIgnoreCase(String word);

    @Query("SELECT d FROM Deutsch d")
    List<Deutsch> getAll();
}
