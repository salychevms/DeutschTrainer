package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.UserDictionary;
import de.salychevms.deutschtrainer.Models.UserLanguage;
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
}
