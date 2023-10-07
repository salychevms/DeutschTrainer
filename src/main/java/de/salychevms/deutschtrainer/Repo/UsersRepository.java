package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    @Transactional
    Users findByTelegramId(String userName);

    @Transactional
    Users findByUserName(String name);
}
