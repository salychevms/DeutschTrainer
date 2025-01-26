package de.salychevms.deutschtrainer.Services;

import de.salychevms.deutschtrainer.TrainerDataBase.Models.Users;
import de.salychevms.deutschtrainer.TrainerDataBase.Repo.UsersRepository;
import de.salychevms.deutschtrainer.TrainerDataBase.Services.UsersService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    void testUpdateNameByTelegramId() {
        Long testTgId = 1234567890000L;
        String testUserName = "testUserName";
        String newTestName = "newTestName";
        Date testDate = new Date();
        Users user = new Users(testTgId, testUserName, testDate);

        when(usersRepository.findByTelegramId(testTgId)).thenReturn(Optional.of(user));
        usersService.updateNameByTelegramId(testTgId, newTestName);

        verify(usersRepository, times(1)).findByTelegramId(testTgId);
        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);

        verify(usersRepository, times(1)).save(userCaptor.capture());
        Users capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getTelegramId()).isEqualTo(testTgId);
        assertThat(capturedUser.getName()).isEqualTo(newTestName);
    }

    @Test
    void testUpdateSurnameByTelegramId() {
        Long testTgId = 54321567890000L;
        String testUserName = "testUserName";
        String newTestSurnameName = "newTestSurnameName";
        Date testDate = new Date();
        Users user = new Users(testTgId, testUserName, testDate);

        when(usersRepository.findByTelegramId(testTgId)).thenReturn(Optional.of(user));
        usersService.updateSurnameByTelegramId(testTgId, newTestSurnameName);

        verify(usersRepository, times(1)).findByTelegramId(testTgId);
        ArgumentCaptor<Users> userCaptor = ArgumentCaptor.forClass(Users.class);

        verify(usersRepository, times(1)).save(userCaptor.capture());
        Users capturedUser = userCaptor.getValue();

        assertThat(capturedUser.getTelegramId()).isEqualTo(testTgId);
        assertThat(capturedUser.getSurname()).isEqualTo(newTestSurnameName);
    }

    @Test
    void testUpdatePhoneNumberByTelegramId() {
        Long testTgId = 223355567890000L;
        String testUserName = "testUserName";
        String newPhoneNumber = "+491571234567";
        Date testDate = new Date();
        Users user = new Users(testTgId, testUserName, testDate);

        when(usersRepository.findByTelegramId(testTgId)).thenReturn(Optional.of(user));
        usersService.updatePhoneNumberByTelegramId(testTgId, newPhoneNumber);

        verify(usersRepository, times(1)).findByTelegramId(testTgId);
        ArgumentCaptor<Users> userCaptor=ArgumentCaptor.forClass(Users.class);

        verify(usersRepository, times(1)).save(userCaptor.capture());
        Users capturedUser=userCaptor.getValue();

        assertThat(capturedUser.getTelegramId()).isEqualTo(testTgId);
        assertThat(capturedUser.getPhoneNumber()).isEqualTo(newPhoneNumber);
    }

    @Test
    void testGetAdminStatus() {
        Long testTgId = 223355567890000L;
        String testUserName = "testUserName";
        Date testDate = new Date();
        Users user = new Users(testTgId, testUserName, testDate);

        user.setAdmin(false);

        when(usersRepository.findByTelegramId(testTgId)).thenReturn(Optional.of(user));
        boolean isAdminFalse=usersService.getAdminStatus(testTgId);
        assertFalse(isAdminFalse);

        user.setAdmin(true);

        when(usersRepository.findByTelegramId(testTgId)).thenReturn(Optional.of(user));
        boolean isAdminTrue=usersService.getAdminStatus(testTgId);
        assertTrue(isAdminTrue);
    }

    @Test
    void testUpdateAdminStatusOn() {
        Long testTgId = 2201030405060L;
        String testUserName = "testUserName";
        Date testDate = new Date();
        Users user = new Users(testTgId, testUserName, testDate);

        when(usersRepository.findByTelegramId(testTgId)).thenReturn(Optional.of(user));
        usersService.updateAdminStatusOn(testTgId);

        verify(usersRepository, times(1)).findByTelegramId(testTgId);
        ArgumentCaptor<Users> userCaptor=ArgumentCaptor.forClass(Users.class);

        verify(usersRepository, times(1)).save(userCaptor.capture());
        Users capturedUser=userCaptor.getValue();

        assertThat(capturedUser.getTelegramId()).isEqualTo(testTgId);
        assertTrue(capturedUser.isAdmin());
    }

    @Test
    void testDeleteUserByTelegramId() {
        Long testTgId = 3304050607080L;
        String testUserName = "testUserName";
        Date testDate = new Date();
        Users user = new Users(testTgId, testUserName, testDate);

        when(usersRepository.findByTelegramId(testTgId)).thenReturn(Optional.of(user));
        usersService.deleteUserByTelegramId(testTgId);

        verify(usersRepository, times(1)).deleteById(testTgId);

        when(usersRepository.findByTelegramId(testTgId)).thenReturn(Optional.empty());

        assertFalse(usersRepository.findByTelegramId(testTgId).isPresent());
    }

    @Test
    void testDeleteAllUsers() {
        Long firstTestTgId = 3123032101230123L;
        String firstTestUserName = "firstTestUserName";
        Date firstTestDate = new Date();
        Users firstUser = new Users(firstTestTgId, firstTestUserName, firstTestDate);

        Long secondTestTgId = 3570357035707530753L;
        String secondTestUserName = "secondTestUserName";
        Date secondTestDate = new Date();
        Users secondUser = new Users(secondTestTgId, secondTestUserName, secondTestDate);

        List<Users> testUserList=new ArrayList<>();
        testUserList.add(firstUser);
        testUserList.add(secondUser);

        when(usersRepository.findAll()).thenReturn(testUserList);
        usersService.deleteAllUsers();

        verify(usersRepository, times(1)).deleteAll();

        when(usersRepository.findAll()).thenReturn(Collections.emptyList());

        assertTrue(usersRepository.findAll().isEmpty());
        assertEquals(Optional.empty(), usersService.findUserByTelegramId(firstTestTgId));
        assertEquals(Optional.empty(), usersService.findUserByTelegramId(secondTestTgId));
    }
}