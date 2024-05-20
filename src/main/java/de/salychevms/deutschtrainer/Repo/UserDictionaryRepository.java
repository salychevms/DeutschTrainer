package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.UserDictionary;
import de.salychevms.deutschtrainer.Models.UserLanguage;
import de.salychevms.deutschtrainer.Models.Users;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDictionaryRepository extends JpaRepository<UserDictionary, Long> {
    @EntityGraph(attributePaths = {"pair"})
    Optional<UserDictionary> getUserDictionaryByPairId(Long id);

    Optional<UserDictionary> findById(Long id);

    List<UserDictionary> getAllByUserLanguage(UserLanguage language);

    Optional<UserDictionary> getUserDictionaryByPairIdAndUserLanguageId(Long pairId, Long userLanguageId);

    //List<UserDictionary> findAllByUserLanguageId(Long userLanguageId);
    @Query("SELECT ud FROM UserDictionary ud WHERE ud.userLanguage.id = :userLanguageId")
    List<UserDictionary> findAllByUserLanguageId(@Param("userLanguageId") Long userLanguageId);

    //get count for all user pairs in user dictionary by telegramId and languageIdentifier
    @Query("SELECT COUNT(ud) FROM UserDictionary ud WHERE ud.userLanguage.user.telegramId = :telegramId AND ud.userLanguage.language.identifier = :languageIdentifier")
    Long countPairsForTelegramIdAndLanguageIdentifier(@Param("telegramId") Long telegramId, @Param("languageIdentifier") String languageIdentifier);

    //get count of unique german words in user dictionary by telegramId and languageIdentifier
    @Query("SELECT COUNT(DISTINCT ud.pair.deutsch.deWord) FROM UserDictionary ud WHERE ud.userLanguage.user.telegramId = :telegramId AND ud.userLanguage.language.identifier = :languageIdentifier")
    Long countUniqueGermanWordsForTelegramIdAndLanguageIdentifier(@Param("telegramId") Long telegramId, @Param("languageIdentifier") String languageIdentifier);
}
