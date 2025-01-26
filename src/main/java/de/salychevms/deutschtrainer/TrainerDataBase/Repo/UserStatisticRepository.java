package de.salychevms.deutschtrainer.TrainerDataBase.Repo;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserDictionary;
import de.salychevms.deutschtrainer.TrainerDataBase.Models.UserStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserStatisticRepository extends JpaRepository<UserStatistic, Long> {

    Optional<UserStatistic> findByWord(UserDictionary userDictionary);

    List<UserStatistic> findAllByNewWordIsTrue();

    @Query("SELECT us FROM UserStatistic us WHERE us.newWord =  false AND us.failStatus = true ORDER BY us.failsAll DESC")
    List<UserStatistic> findAllOrderByFailsAllDesc();

    @Query("SELECT us FROM UserStatistic us WHERE us.newWord =  false ORDER BY us.iterationsAll ASC")
    List<UserStatistic> findAllOrderByIterationsAllAsc();

    // count of new words to learn by telegramId and languageIdentifier
    @Query("SELECT COUNT(us) FROM UserStatistic us JOIN us.word ud JOIN ud.userLanguage ul JOIN ul.language l JOIN ul.user u WHERE u.telegramId = :telegramId AND l.identifier = :languageIdentifier AND us.newWord = true")
    Long countPairsWithNewWordForUserAndLanguage(@Param("telegramId") Long telegramId, @Param("languageIdentifier") String languageIdentifier);

    // pairs list for pairs which have maximum fails
    @Query("SELECT us FROM UserStatistic us " +
            "WHERE us.word.userLanguage.user.telegramId = :telegramId " +
            "AND us.word.userLanguage.language.identifier = :languageIdentifier " +
            "AND us.failsAll = (SELECT MAX(us2.failsAll) FROM UserStatistic us2 WHERE us2.word.userLanguage.user.telegramId = :telegramId AND us2.word.userLanguage.language.identifier = :languageIdentifier)")
    List<UserStatistic> findWordsWithMaxFailsAllForUserAndLanguageIdentifier(Long telegramId, String languageIdentifier);

    @Query("SELECT MAX(us.lastTraining) FROM UserStatistic us JOIN us.word ud JOIN ud.userLanguage ul JOIN ul.language l JOIN ul.user u WHERE u.telegramId = :telegramId AND l.identifier = :languageIdentifier")
    Date findLastTrainingForUserAndLanguage(@Param("telegramId") Long telegramId, @Param("languageIdentifier") String languageIdentifier);

    @Query("SELECT COUNT(us) FROM UserStatistic us " +
            "WHERE us.word.userLanguage.user.telegramId = :telegramId " +
            "AND us.word.userLanguage.language.identifier = :languageIdentifier " +
            "AND us.failStatus = true")
    int countWordsWithFailStatusForUserAndLanguage(Long telegramId, String languageIdentifier);
}
