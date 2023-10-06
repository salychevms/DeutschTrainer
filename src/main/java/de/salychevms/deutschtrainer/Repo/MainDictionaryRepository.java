package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.MainDictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainDictionaryRepository extends JpaRepository<MainDictionary, Long> {
}
