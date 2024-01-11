package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Services.UsersService;
import org.apache.commons.codec.language.bm.Lang;
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

    public void updateNameByTelegramID(long telegramId, String name) {
        usersService.updateNameByTelegramID(telegramId, name);
    }

    public void updateSurnameByTelegramId(long telegramId, String surname) {
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
}
