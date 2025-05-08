package de.salychevms.deutschtrainer.TrainerDataBase.Repo;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Deutsch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeutschRepository extends JpaRepository<Deutsch, Long> {
    Optional<Deutsch> findByDeWordIgnoreCase(String word);

    Optional<Deutsch> findByDeWord(String word);

    @Query("SELECT d FROM Deutsch d")
    List<Deutsch> getAll();

    @Query(value = """
    SELECT * FROM deutsch
    WHERE lower(de_word) LIKE CONCAT('%', lower(:input), '%')
    ORDER BY 
        CASE 
            WHEN lower(de_word) = lower(:input) THEN 0
            WHEN lower(de_word) LIKE lower(:input) || '%' THEN 1
            ELSE 2
        END,
        length(de_word)
    LIMIT 20
    """, nativeQuery = true)
    List<Deutsch> findSmartMatches(@Param("input") String input);
}
