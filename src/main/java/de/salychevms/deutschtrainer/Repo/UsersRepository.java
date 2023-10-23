package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.Users;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByUserName(String userName);

    Users findByName(String name);

    Users findBySurname(String surname);

    Users findByTelegramId(Long telegramId);
}
