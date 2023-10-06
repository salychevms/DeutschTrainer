package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {
}
