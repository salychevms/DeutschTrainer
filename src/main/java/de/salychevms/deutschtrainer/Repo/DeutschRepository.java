package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.Deutsch;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeutschRepository extends JpaRepository<Deutsch, Long> {
    @Transactional
    Optional<Deutsch> findByDeWord(String word);

}
