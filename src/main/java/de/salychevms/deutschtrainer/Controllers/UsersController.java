package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.UsersRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Optional;
@RestController
public class UsersController {
    final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean registeredOr (String userName){
        Users user= usersRepository.findByTelegramId(userName);
        return user != null;
    }

    public void createNewUser(String userName){
        Users newUser=new Users();
        newUser.setTelegramId(userName);
        newUser.setUserName(userName);
        newUser.setRegistrationDate(new Date());
        usersRepository.save(newUser);
    }
}
