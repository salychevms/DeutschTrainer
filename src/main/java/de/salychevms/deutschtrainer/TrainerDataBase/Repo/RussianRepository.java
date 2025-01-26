package de.salychevms.deutschtrainer.TrainerDataBase.Repo;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Russian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RussianRepository extends JpaRepository<Russian, Long> {
    Optional<Russian> findByRuWordIgnoreCase(String ruWord);

    Optional<Russian> findByRuWord(String word);

    List<Russian> findByRuWordContainingIgnoreCase(String word);

    @Query(value = "SELECT * FROM russian ORDER BY RANDOM() LIMIT 3", nativeQuery = true)
    List<Russian> find3RandomRussian();

    @Query(value = "SELECT * FROM russian ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Optional<Russian> find1RandomRussian();
}
