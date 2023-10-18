package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.Users;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @Transactional
    Users findByUserName(String userName);

    @Transactional
    Users findByName(String name);

    @Transactional
    Users findBySurname(String surname);

    @Transactional
    Users findByTelegramId(Long telegramId);
}
