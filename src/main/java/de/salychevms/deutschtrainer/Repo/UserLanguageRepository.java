package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.UserLanguage;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
    @Transactional
    List<UserLanguage> findAllByUserId(Long userId);

    @Transactional
    Optional<UserLanguage> findAllByUserIdAndLanguageId(Long userId, Long languageId);

    @Transactional
    List<UserLanguage> findAllByLanguageId(Long languageId);

    @Transactional
    List<UserLanguage> findAllByLanguageName(String name);
}
