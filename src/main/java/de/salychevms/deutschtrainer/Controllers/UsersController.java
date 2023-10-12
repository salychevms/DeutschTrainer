package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.UsersRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class UsersController {
    final UsersRepository usersRepository;

    public UsersController(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean registeredOr(String userName) {
        Users user = usersRepository.findByUserName(userName);
        return user != null;
    }

    public void createNewUser(String userName) {
        Users newUser = new Users();
        newUser.setUserName(userName);
        newUser.setName(userName);
        newUser.setRegistrationDate(new Date());
        usersRepository.save(newUser);
    }

    public Optional<Users> findUserById(Long id) {
        return usersRepository.findById(id);
    }

    public Optional<Users> findUserByUsername(String userName) {
        return Optional.ofNullable(usersRepository.findByUserName(userName));
    }

    public List<Users> findAllUsers() {
        return usersRepository.findAll();
    }

    public boolean updateNameByUserName(String userName, String name) {
        Optional<Users> user = Optional.ofNullable(usersRepository.findByUserName(userName));
        if (user.isPresent()) {
            Users update = user.get();
            update.setName(name);
            usersRepository.save(update);
            return true;
        } else return false;
    }

    public boolean updateSurnameByUserName(String userName, String surname) {
        Optional<Users> user = Optional.ofNullable(usersRepository.findByUserName(userName));
        if (user.isPresent()) {
            Users update = user.get();
            update.setSurname(surname);
            usersRepository.save(update);
            return true;
        } else return false;
    }

    public boolean updatePhoneNumber(Long id, String number) {
        Optional<Users> user = usersRepository.findById(id);
        if (user.isPresent()) {
            Users update = user.get();
            update.setPhoneNumber(number);
            usersRepository.save(update);
            return true;
        } else return false;
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
