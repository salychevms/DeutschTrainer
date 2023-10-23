package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.MessageHistory;
import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.MessageHistoryRepository;
import de.salychevms.deutschtrainer.Repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class MessageHistoryController {
    final MessageHistoryRepository messageHistoryRepository;
    final UsersController usersController;

    @Autowired
    public MessageHistoryController(MessageHistoryRepository messageHistoryRepository, UsersController usersController) {
        this.messageHistoryRepository = messageHistoryRepository;
        this.usersController = usersController;
    }

    public boolean saveData(Long userId, long chatId, String content, boolean isMessage, boolean isCallbackData) {
        if (!(!isMessage && !isCallbackData)) {
            if (isMessage != isCallbackData) {
                Users user = usersController.findUserById(userId).get();
                MessageHistory messageHistory = new MessageHistory(user, chatId, content, isMessage, isCallbackData);
                messageHistoryRepository.save(messageHistory);
                deleteLastHistory(userId);
                return true;
            } else return false;
        } else return false;
    }

    public List<MessageHistory> getAllHistory() {
        return messageHistoryRepository.findAll();
    }

    public List<MessageHistory> getAllHistoryByUser(Long userId) {
        Optional<Users> user = usersController.findUserById(userId);
        return user.map(messageHistoryRepository::findMessageHistoriesByUser).orElse(null);
    }

    public List<MessageHistory> getAllHistoryByChatId(Long chatId) {
        return messageHistoryRepository.findAllByChatId(chatId);
    }

    private void deleteLastHistory(Long userId) {
        List<MessageHistory> all = getAllHistoryByUser(userId);
        int totalValues = all.size();
        int valuesToKeep = Math.min(totalValues, totalValues - 100);

        if (totalValues > valuesToKeep) {
            all.subList(valuesToKeep, totalValues).clear();
            if (!all.isEmpty()) {
                messageHistoryRepository.deleteAll(all);
            }
        }
    }
}
