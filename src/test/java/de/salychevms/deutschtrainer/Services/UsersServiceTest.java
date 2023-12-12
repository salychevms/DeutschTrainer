package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.Models.Users;
import de.salychevms.deutschtrainer.Repo.UsersRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class UsersServiceTest {
    @Mock
    private UsersRepository usersRepository;
    @InjectMocks
    private UsersService usersService;

    @Test
    void testRegisteredOr() {
        Long testTgId = 1234567890000L;
        String testUserName = "testUserName";
        Date testDate = new Date();
        Users registeredUser = new Users(testTgId, testUserName, testDate);

        Long justTgId = 987654321000000L;

        when(usersRepository.findByTelegramId(registeredUser.getTelegramId())).thenReturn(Optional.of(registeredUser));
        when(usersRepository.findByTelegramId(justTgId)).thenReturn(Optional.empty());

        assertTrue(usersService.registeredOr(registeredUser.getTelegramId()));
        assertFalse(usersService.registeredOr(justTgId));
    }

    @Test
    void testCreateNewUser() {
        Long testTgId = 1234567890000L;
        String testUserName = "testUserName";

        usersService.createNewUser(testTgId, testUserName);
        verify(usersRepository, times(1)).save(argThat(newUser ->
                newUser.getTelegramId().equals(testTgId) &&
                        newUser.getUserName().equals(testUserName)));

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        verify(usersRepository, times(1)).save(userCaptor.capture());

        Users capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getTelegramId()).isEqualTo(testTgId);
        assertThat(capturedUser.getUserName()).isEqualTo(testUserName);
        assertNotNull(capturedUser.getRegistrationDate());
    }

    @Test
    void testFindUserByTelegramId() {
        Long testTgId = 1234567890000L;
        String testUserName = "testUserName";
        Date testDate = new Date();
        Users user = new Users(testTgId, testUserName, testDate);

        when(usersRepository.findByTelegramId(testTgId)).thenReturn(Optional.of(user));
        Optional<Users> result = usersService.findUserByTelegramId(testTgId);

        result.ifPresent(users -> assertEquals(user, users));
    }

    @Test
    void updateNameByTelegramId() {
        Long testTgId = 1234567890000L;
        String testUserName = "testUserName";
        Date testDate = new Date();
        Users user = new Users(testTgId, testUserName, testDate);

        assertNull(user.getName());

        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);
        doAnswer(invocation -> {
            Users savedUser = invocation.getArgument(0);
            savedUser.setName("newTestName");
            return null;
        }).when(usersRepository).save(userCaptor.capture());

        String newTestName = "newTestName";
        usersService.updateNameByTelegramID(testTgId, newTestName);

        verify(usersRepository, times(1)).save(any(Users.class));
        Users capturedUser = userCaptor.getValue();

        assertThat    (capturedU ser.getName()).isEqualTo(newTestName);
    }

    @Test
    void updateSurnameByTelegramId() {
    }

    @Test
    void updatePhoneNumberByTelegramId() {
    }

    @Test
    void getAdminStatus() {
    }

    @Test
    void updateAdminStatusOn() {
    }

    @Test
    void deleteUserByTelegramId() {
    }

    @Test
    void deleteAllUsers() {
    }
}