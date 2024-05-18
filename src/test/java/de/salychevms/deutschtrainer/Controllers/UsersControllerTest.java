package de.salychevms.deutschtrainer.Controllers;

import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Services.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsersControllerTest {
    @Mock
    private UsersService usersService;
    @InjectMocks
    private UsersController usersController;

    @Test
    void testRegisteredOrIfYes() {
        Long tgId = 12345L;

        when(usersService.registeredOr(tgId)).thenReturn(true);
        boolean registeredOr = usersController.registeredOr(tgId);

        verify(usersService, times(1)).registeredOr(tgId);
        assertTrue(registeredOr);
    }

    @Test
    void testRegisteredOrIfNo() {
        Long tgId = 12345L;

        when(usersService.registeredOr(tgId)).thenReturn(false);
        boolean registeredOr = usersController.registeredOr(tgId);

        verify(usersService, times(1)).registeredOr(tgId);
        assertFalse(registeredOr);
    }

    @Test
    void testCreateNewUser() {
        Long tgId = 6547989456L;
        String usrName = "usrName";

        usersController.createNewUser(tgId, usrName);
        verify(usersService, times(1)).createNewUser(tgId, usrName);
    }

    @Test
    void testFindUserByTelegramId() {
        Long telegramId=5556664433222L;
        String usrName="usrName";
        Users user=new Users(telegramId, usrName, new Date());

        when(usersService.findUserByTelegramId(telegramId)).thenReturn(Optional.of(user));
        Optional<Users> result=usersController.findUserByTelegramId(telegramId);

        result.ifPresent(value-> assertEquals(user, value));
    }

    @Test
    void testFindUserByTelegramIdIfUserDoesNotExist() {
        Long telegramId=5556664433222L;

        when(usersService.findUserByTelegramId(telegramId)).thenReturn(Optional.empty());
        Optional<Users> result=usersController.findUserByTelegramId(telegramId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateNameByTelegramId() {
        long tgId=9988777777777L;
        String newUserName="newName";

        usersController.updateNameByTelegramId(tgId, newUserName);
        verify(usersService, times(1)).updateNameByTelegramId(tgId, newUserName);
    }

    @Test
    void testUpdateSurnameByTelegramId() {
        long tgId=7777777779988L;
        String newUserSurName="newSurName";

        usersController.updateSurnameByTelegramId(tgId, newUserSurName);
        verify(usersService, times(1)).updateSurnameByTelegramId(tgId, newUserSurName);
    }

    @Test
    void testUpdatePhoneNumberByTelegramId() {
        Long tgId=7777777779988L;
        String phone="+4987654321000";

        usersController.updatePhoneNumberByTelegramId(tgId, phone);
        verify(usersService, times(1)).updatePhoneNumberByTelegramId(tgId, phone);
    }

    @Test
    void testGetAdminStatusIsTrue() {
        Long tgId=55555555555L;

        when(usersService.getAdminStatus(tgId)).thenReturn(true);
        boolean result=usersController.getAdminStatus(tgId);

        assertTrue(result);
    }

    @Test
    void testGetAdminStatusIsFalse() {
        Long tgId=55555555555L;

        when(usersService.getAdminStatus(tgId)).thenReturn(false);
        boolean result=usersController.getAdminStatus(tgId);

        assertFalse(result);
    }

    @Test
    void updateAdminStatusOn() {
        Long id=777999888888L;

        usersController.updateAdminStatusOn(id);
        verify(usersService, times(1)).updateAdminStatusOn(id);
    }

    @Test
    void deleteUserById() {
        Long tgId=666666666666L;

        usersController.deleteUserById(tgId);
        verify(usersService, times(1)).deleteUserByTelegramId(tgId);
    }

    @Test
    void deleteAllUsers() {
        usersController.deleteAllUsers();
        verify(usersService, times(1)).deleteAllUsers();
    }
}