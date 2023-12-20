package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLanguageRepository extends JpaRepository<UserLanguage, Long> {
    ///WARNING! this annotation helps to get the data from language.table
    @EntityGraph(attributePaths = "language")
    List<UserLanguage> findAllByUser_TelegramId(Long telegramId);

    Optional<UserLanguage> findByUserAndLanguageId(Users user, Long languageId);
}
