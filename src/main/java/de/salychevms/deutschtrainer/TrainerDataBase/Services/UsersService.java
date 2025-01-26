package de.salychevms.deutschtrainer.TrainerDataBase.Services;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Users;
import de.salychevms.deutschtrainer.TrainerDataBase.Repo.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;

    public UsersService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Transactional
    public boolean registeredOr(Long telegramId) {
        Optional<Users> user = usersRepository.findByTelegramId(telegramId);
        return user.isPresent();
    }

    @Transactional
    public void createNewUser(Long telegramId, String userName) {
        Users newUser = new Users();
        newUser.setTelegramId(telegramId);
        newUser.setUserName(userName);
        newUser.setRegistrationDate(new Date());
        usersRepository.save(newUser);
    }


    @Transactional
    public Optional<Users> findUserByTelegramId(Long telegramId) {
        return usersRepository.findByTelegramId(telegramId);
    }

    @Transactional
    public void updateNameByTelegramId(long telegramId, String name) {
        Optional<Users> user = (usersRepository.findByTelegramId(telegramId));
        if (user.isPresent()) {
            Users update = user.get();
            update.setName(name);
            usersRepository.save(update);
        }
    }

    @Transactional
    public void updateSurnameByTelegramId(long telegramId, String surname) {
        Optional<Users> user = (usersRepository.findByTelegramId(telegramId));
        if (user.isPresent()) {
            Users update = user.get();
            update.setSurname(surname);
            usersRepository.save(update);
        }
    }

    @Transactional
    public void updatePhoneNumberByTelegramId(Long telegramId, String number) {
        Optional<Users> user = (usersRepository.findByTelegramId(telegramId));
        if (user.isPresent()) {
            Users update = user.get();
            update.setPhoneNumber(number);
            usersRepository.save(update);
        }
    }

    @Transactional
    public boolean getAdminStatus(Long telegramId) {
        Optional<Users> user = usersRepository.findByTelegramId(telegramId);
        return user.map(Users::isAdmin).orElse(false);
    }

    @Transactional
    public void updateAdminStatusOn(Long telegramId) {
        Optional<Users> user = usersRepository.findByTelegramId(telegramId);
        if (user.isPresent()) {
            Users update = user.get();
            if (!update.isAdmin()) {
                update.setAdmin(true);
                usersRepository.save(update);
            }
        }
    }

    @Transactional
    public void deleteUserByTelegramId(Long telegramId) {
        Optional<Users> user=usersRepository.findByTelegramId(telegramId);
        if (user.isPresent()) {
            usersRepository.deleteById(telegramId);
        }
    }

    @Transactional
    public void deleteAllUsers() {
        List<Users> deleted = usersRepository.findAll();
        if (!deleted.isEmpty()) {
            usersRepository.deleteAll();
        }
    }
}
