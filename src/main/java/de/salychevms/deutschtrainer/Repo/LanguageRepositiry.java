package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.Language;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepositiry extends JpaRepository<Language, Long> {
}
