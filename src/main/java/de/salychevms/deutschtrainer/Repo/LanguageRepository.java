package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.Language;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long> {
    Optional<Language> findByName(String name);
    Optional<Language> findLanguageByIdentifier(String identifier);
}
