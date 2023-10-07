package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.Language;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    @Transactional
    Optional<Language> findByName(String name);
    @Transactional
    Optional<Language> findByIdentifier(String identifier);
}
