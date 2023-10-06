package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.UserLanguage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
}
