package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.UserDictionary;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDictionaryRepository extends JpaRepository<UserDictionary, Long> {

}
