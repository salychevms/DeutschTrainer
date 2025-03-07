package de.salychevms.deutschtrainer.TrainerDataBase.Controllers;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Users;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsersController {
    private final UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    public boolean registeredOr(Long telegramId) {
        return usersService.registeredOr(telegramId);
    }

    public void createNewUser(Long telegramId, String userName) {
        usersService.createNewUser(telegramId, userName);
    }

    public Optional<Users> findUserByTelegramId(Long telegramId) {
        return usersService.findUserByTelegramId(telegramId);
    }

    public void updateNameByTelegramId(Long telegramId, String name) {
        usersService.updateNameByTelegramId(telegramId, name);
    }

    public void updateSurnameByTelegramId(Long telegramId, String surname) {
        usersService.updateSurnameByTelegramId(telegramId, surname);
    }

    public void updatePhoneNumberByTelegramId(Long telegramId, String number) {
        usersService.updatePhoneNumberByTelegramId(telegramId, number);
    }

    public boolean getAdminStatus(Long telegramId) {
        return usersService.getAdminStatus(telegramId);
    }

    public void updateAdminStatusOn(Long telegramId) {
        usersService.updateAdminStatusOn(telegramId);
    }

    public void deleteUserById(Long telegramId) {
        usersService.deleteUserByTelegramId(telegramId);
    }

    public void deleteAllUsers() {
        usersService.deleteAllUsers();
    }

    public boolean isAdminChecker(Long telegramId) {
        Optional<Users> user = usersService.findUserByTelegramId(telegramId);
        return user.map(Users::isAdmin).orElse(false);
    }
}
