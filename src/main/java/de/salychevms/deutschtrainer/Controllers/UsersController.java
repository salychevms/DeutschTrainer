package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.UsersRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
@RestController
public class UsersController {
    final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean registeredOr (String userName){
        Users user= usersRepository.findByUserName(userName);
        return user != null;
    }
}
