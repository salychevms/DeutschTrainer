package de.salychevms.deutschtrainer.Repo;

import de.salychevms.deutschtrainer.Models.MessageHistory;
import de.salychevms.deutschtrainer.Models.Users;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageHistoryRepository extends JpaRepository<MessageHistory, Long> {
    List<MessageHistory> findMessageHistoriesByUser(Users user);

    List<MessageHistory> findAllByChatId(Long chatId);


}
