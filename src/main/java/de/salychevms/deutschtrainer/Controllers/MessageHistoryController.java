package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Repo.MessageHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageHistoryController {
    final MessageHistoryRepository messageHistoryRepository;
    final UsersController usersController;

    @Autowired
    public MessageHistoryController(MessageHistoryRepository messageHistoryRepository, UsersController usersController) {
        this.messageHistoryRepository = messageHistoryRepository;
        this.usersController = usersController;
    }
}
