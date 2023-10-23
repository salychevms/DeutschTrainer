package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.Russian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RussianRepository extends JpaRepository<Russian, Long> {
    Optional<Russian> findByRuWordIgnoreCase(String ruWord);
}
