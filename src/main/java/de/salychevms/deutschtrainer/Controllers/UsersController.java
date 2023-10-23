package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class UsersController {
    final UsersRepository usersRepository;

    @Autowired
    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean registeredOr(Long telegramId) {
        Users user = usersRepository.findByTelegramId(telegramId);
        return user != null;
    }

    public void createNewUser(Long telegramId, String userName) {
        Users newUser = new Users();
        newUser.setTelegramId(telegramId);
        newUser.setUserName(userName);
        newUser.setRegistrationDate(new Date());
        usersRepository.save(newUser);
    }

    public Optional<Users> findUserById(Long id) {
        return usersRepository.findById(id);
    }

    public Optional<Users> findUserByTelegramId(Long telegramId) {
        return Optional.ofNullable(usersRepository.findByTelegramId(telegramId));
    }

    public Optional<Users> findUserByUsername(String userName) {
        return Optional.ofNullable(usersRepository.findByUserName(userName));
    }

    public List<Users> findAllUsers() {
        return usersRepository.findAll();
    }

    public boolean updateNameByTelegramID(long telegramId, String name) {
        Optional<Users> user = Optional.ofNullable(usersRepository.findByTelegramId(telegramId));
        if (user.isPresent()) {
            Users update = user.get();
            update.setName(name);
            usersRepository.save(update);
            return true;
        } else return false;
    }

    public void updateSurnameByTelegramId(long telegramId, String surname) {
        Optional<Users> user = Optional.ofNullable(usersRepository.findByTelegramId(telegramId));
        if (user.isPresent()) {
            Users update = user.get();
            update.setSurname(surname);
            usersRepository.save(update);
        }
    }

    public boolean updatePhoneNumberByTelegramId(Long telegramId, String number) {
        Optional<Users> user = Optional.ofNullable(usersRepository.findByTelegramId(telegramId));
        if (user.isPresent()) {
            Users update = user.get();
            update.setPhoneNumber(number);
            usersRepository.save(update);
            return true;
        } else return false;
    }

    public boolean getAmdinStatus(Long telegramId) {
        Optional<Users> user = usersRepository.findById(telegramId);
        return user.map(Users::isAdmin).orElse(false);
    }

    public boolean updateAdminStatusOn(Long id) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isPresent()) {
            Users update = user.get();
            if (!update.isAdmin()) {
                update.setAdmin(true);
                usersRepository.save(update);
                return true;
            } else return false;
        } else return false;
    }

    public boolean deleteUserById(Long id) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isPresent()) {
            usersRepository.deleteById(id);
            return true;
        } else return false;
    }

    public boolean deleteAllUsers() {
        List<Users> deleted = usersRepository.findAll();
        if (!deleted.isEmpty()) {
            usersRepository.deleteAll();
            return true;
        } else return false;
    }
}
