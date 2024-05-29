package ait.cohort34.accounting.service;

import ait.cohort34.accounting.dao.RoleRepository;
import ait.cohort34.accounting.dao.UserAccountRepository;
import ait.cohort34.accounting.dto.NewPasswordDto;
import ait.cohort34.accounting.dto.UserDto;
import ait.cohort34.accounting.dto.UserEditDto;
import ait.cohort34.accounting.dto.UserRegisterDto;
import ait.cohort34.accounting.model.Role;
import ait.cohort34.accounting.model.UserAccount;
import ait.cohort34.petPosts.dao.PetRepository;
import ait.cohort34.petPosts.model.Pet;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;


//    @Test
//    public void testRegisterUser() {
//        UserRegisterDto userRegisterDto = new UserRegisterDto();
//        userRegisterDto.setLogin("testuser");
//        userRegisterDto.setPassword("password");
//
//        UserAccount userAccount = new UserAccount();
//        userAccount.setLogin("testuser");
//
//        when(userAccountRepository.existsByLogin("testuser")).thenReturn(false);
//        when(roleRepository.findByTitle("ROLE_USER")).thenReturn(new Role("ROLE_USER"));
//        when(modelMapper.map(userRegisterDto, UserAccount.class)).thenReturn(userAccount);
//        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
//
//        UserDto userDto = new UserDto();
//        when(modelMapper.map(userAccount, UserDto.class)).thenReturn(userDto);
//
//        UserDto result = userAccountService.register(userRegisterDto);
//
//        assertEquals(userDto, result);
//        verify(userAccountRepository).save(userAccount);
//    }


    @Test
    public void testGetUser() {
        String login = "testuser";
        UserAccount userAccount = new UserAccount();
        userAccount.setLogin(login);

        when(userAccountRepository.findByLogin(login)).thenReturn(Optional.of(userAccount));

        UserDto userDto = new UserDto();
        when(modelMapper.map(userAccount, UserDto.class)).thenReturn(userDto);

        UserDto result = userAccountService.getUser(login);

        assertEquals(userDto, result);
    }


//    @Test
//    public void testUpdateUser() {
//        Long userId = 1L;
//        UserEditDto userEditDto = new UserEditDto();
//        userEditDto.setFullName("New Name");
//
//        UserAccount userAccount = new UserAccount();
//        userAccount.setId(userId);
//        userAccount.setFullName("Old Name");
//
//        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
//
//        UserDto userDto = new UserDto();
//        when(modelMapper.map(userAccount, UserDto.class)).thenReturn(userDto);
//
//        UserDto result = userAccountService.updateUser(userId, userEditDto);
//
//        assertEquals("New Name", userAccount.getFullName());
//        assertEquals(userDto, result);
//        verify(userAccountRepository).save(userAccount);
//    }


    @Test
    public void testRemoveUser() {
        Long userId = 1L;
        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);

        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        List<Pet> pets = Collections.emptyList();
        when(petRepository.findByAuthorIgnoreCase(anyString())).thenReturn(pets.stream());

        UserDto userDto = new UserDto();
        when(modelMapper.map(userAccount, UserDto.class)).thenReturn(userDto);

        UserDto result = userAccountService.removeUser(userId);

        assertEquals(userDto, result);
        verify(userAccountRepository).delete(userAccount);
        verify(petRepository).deleteAll(pets);
    }


    @Test
    public void testChangePassword() {
        String login = "testuser";
        NewPasswordDto passwordDto = new NewPasswordDto();
        passwordDto.setOldPassword("oldPassword");
        passwordDto.setNewPassword("newPassword");

        UserAccount userAccount = new UserAccount();
        userAccount.setLogin(login);
        userAccount.setPassword("encodedOldPassword");

        when(userAccountRepository.findByLogin(login)).thenReturn(Optional.of(userAccount));
        when(passwordEncoder.matches("oldPassword", "encodedOldPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");

        userAccountService.changePassword(login, passwordDto);

        assertEquals("encodedNewPassword", userAccount.getPassword());
        verify(userAccountRepository).save(userAccount);
    }

}
