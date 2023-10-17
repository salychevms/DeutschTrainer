package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.MessageHistory;
import de.salychevms.deutschtrainer.Models.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageHistoryRepository extends JpaRepository<MessageHistory, Long> {
    @Transactional
    List<MessageHistory> findMessageHistoriesByUser(Users user);

    @Transactional
    List<MessageHistory> findAllByChatId(Long chatId);


}
