package de.salychevms.deutschtrainer.TrainerDataBase.Repo;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserName(String userName);

    Optional<Users> findByName(String name);

    Optional<Users> findBySurname(String surname);

    Optional<Users> findByTelegramId(Long telegramId);

}
