package ait.cohort34.accounting.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import ait.cohort34.accounting.dao.RoleRepository;
import ait.cohort34.accounting.dao.UserAccountRepository;
import ait.cohort34.accounting.dao.UserPhotoRepository;
import ait.cohort34.accounting.dto.NewPasswordDto;
import ait.cohort34.accounting.dto.UserDto;
import ait.cohort34.accounting.dto.UserEditDto;
import ait.cohort34.accounting.dto.UserRegisterDto;
import ait.cohort34.accounting.dto.exceptions.UserNotFoundException;
import ait.cohort34.accounting.model.PhotoUser;
import ait.cohort34.accounting.model.Role;
import ait.cohort34.accounting.model.UserAccount;
import ait.cohort34.petPosts.dao.PetRepository;
import ait.cohort34.petPosts.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashSet;


public class UserAccountServiceImplTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private PetRepository petRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserPhotoRepository photoRepository;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() throws Exception {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setLogin("user1");
        userRegisterDto.setPassword("password");

        when(userAccountRepository.existsByLogin("user1")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Role userRole = new Role("ROLE_USER");
        when(roleRepository.findByTitle("ROLE_USER")).thenReturn(userRole);

        UserAccount userAccount = new UserAccount();
        userAccount.setLogin("user1");
        userAccount.setPassword("encodedPassword");
        userAccount.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        when(modelMapper.map(userRegisterDto, UserAccount.class)).thenReturn(userAccount);
        when(userAccountRepository.save(userAccount)).thenReturn(userAccount);

        UserDto userDto = new UserDto();
        when(modelMapper.map(userAccount, UserDto.class)).thenReturn(userDto);

        UserDto result = userAccountService.register(userRegisterDto, null);

        assertNotNull(result);
        verify(userAccountRepository, times(1)).existsByLogin("user1");
        verify(passwordEncoder, times(1)).encode("password");
        verify(roleRepository, times(1)).findByTitle("ROLE_USER");
        verify(userAccountRepository, times(1)).save(userAccount);
    }

    @Test
    void testGetUsers() {
        UserAccount user1 = new UserAccount();
        user1.setLogin("user1");

        UserAccount user2 = new UserAccount();
        user2.setLogin("admin");

        when(userAccountRepository.findAll()).thenReturn(List.of(user1, user2));
        UserDto userDto1 = new UserDto();
        when(modelMapper.map(user1, UserDto.class)).thenReturn(userDto1);

        List<UserDto> users = userAccountService.getUsers();

        assertEquals(1, users.size());
        assertEquals(userDto1, users.get(0));
        verify(modelMapper, times(1)).map(user1, UserDto.class);
        verify(modelMapper, never()).map(user2, UserDto.class);
    }

    @Test
    void testGetUser() {
        UserAccount userAccount = new UserAccount();
        userAccount.setLogin("user1");

        when(userAccountRepository.findByLogin("user1")).thenReturn(Optional.of(userAccount));
        UserDto userDto = new UserDto();
        when(modelMapper.map(userAccount, UserDto.class)).thenReturn(userDto);

        UserDto result = userAccountService.getUser("user1");

        assertNotNull(result);
        verify(userAccountRepository, times(1)).findByLogin("user1");
        verify(modelMapper, times(1)).map(userAccount, UserDto.class);
    }

    @Test
    void testRemoveUser() {
        UserAccount userAccount = new UserAccount();
        userAccount.setId(1L);
        userAccount.setLogin("user1");

        when(userAccountRepository.findById(1L)).thenReturn(Optional.of(userAccount));
        List<Pet> petList = new ArrayList<>();
        when(petRepository.findByAuthorIgnoreCase("user1")).thenReturn(petList.stream());

        UserDto userDto = new UserDto();
        when(modelMapper.map(userAccount, UserDto.class)).thenReturn(userDto);

        UserDto result = userAccountService.removeUser(1L);

        assertNotNull(result);
        verify(userAccountRepository, times(1)).findById(1L);
        verify(userAccountRepository, times(1)).delete(userAccount);
        verify(petRepository, times(1)).deleteAll(petList);
        verify(modelMapper, times(1)).map(userAccount, UserDto.class);
    }

    @Test
    public void testUpdateUser() throws IOException {
        // Mock data
        Long userId = 1L;
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setFullName("New FullName");
        userEditDto.setEmail("new.email@example.com");
        userEditDto.setPhone("123456789");
        userEditDto.setTelegram("@newTelegram");
        userEditDto.setWebsite("http://newwebsite.com");

        UserAccount userAccount = new UserAccount();
        userAccount.setId(userId);
        userAccount.setFullName("Old FullName");
        userAccount.setEmail("old.email@example.com");

        PhotoUser photoUser = new PhotoUser();
        photoUser.setId(1L);
        userAccount.setAvatar(photoUser);

        MultipartFile image = mock(MultipartFile.class);
        byte[] newPhotoData = "new photo data".getBytes();
        when(image.isEmpty()).thenReturn(false);
        when(image.getBytes()).thenReturn(newPhotoData);

        when(userAccountRepository.findById(userId)).thenReturn(Optional.of(userAccount));
        when(userAccountRepository.save(userAccount)).thenReturn(userAccount);

        UserDto userDto = new UserDto();
        userDto.setFullName("New FullName");
        userDto.setEmail("new.email@example.com");
        when(modelMapper.map(userAccount, UserDto.class)).thenReturn(userDto);

        // Call the method under test
        UserDto result = userAccountService.updateUser(userId, userEditDto, image);

        // Verify interactions
        verify(userAccountRepository, times(1)).findById(userId);
        verify(userAccountRepository, times(1)).save(userAccount);
        verify(modelMapper, times(1)).map(userAccount, UserDto.class);

        // Verify the result
        assertEquals("New FullName", result.getFullName());
        assertEquals("new.email@example.com", result.getEmail());
        assertEquals(newPhotoData, userAccount.getAvatar().getData());
    }

    @Test
    public void testUpdateUser_UserNotFound() {
        Long userId = 1L;
        UserEditDto userEditDto = new UserEditDto();
        MultipartFile image = mock(MultipartFile.class);

        when(userAccountRepository.findById(userId)).thenReturn(Optional.empty());

        // Verify the exception is thrown
        assertThrows(UserNotFoundException.class, () -> {
            userAccountService.updateUser(userId, userEditDto, image);
        });

        // Verify interactions
        verify(userAccountRepository, times(1)).findById(userId);
        verify(userAccountRepository, never()).save(any(UserAccount.class));
        verify(modelMapper, never()).map(any(UserAccount.class), eq(UserDto.class));
    }

    @Test
    public void testChangePassword_Success() {
        // Mock data
        String login = "user";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        UserAccount userAccount = new UserAccount();
        userAccount.setLogin(login);
        userAccount.setPassword(oldPassword);

        NewPasswordDto passwordDto = new NewPasswordDto();
        passwordDto.setOldPassword(oldPassword);
        passwordDto.setNewPassword(newPassword);

        when(userAccountRepository.findByLogin(login)).thenReturn(Optional.of(userAccount));
        when(passwordEncoder.matches(oldPassword, oldPassword)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

        // Call the method under test
        userAccountService.changePassword(login, passwordDto);

        // Verify interactions
        verify(userAccountRepository, times(1)).findByLogin(login);
        verify(passwordEncoder, times(1)).matches(oldPassword, oldPassword);
        verify(passwordEncoder, times(1)).encode(newPassword);
        verify(userAccountRepository, times(1)).save(userAccount);
    }

    @Test
    public void testChangePassword_UserNotFound() {
        // Mock data
        String login = "user";
        NewPasswordDto passwordDto = new NewPasswordDto();

        when(userAccountRepository.findByLogin(login)).thenReturn(Optional.empty());

        // Verify the exception is thrown
        assertThrows(UsernameNotFoundException.class, () -> {
            userAccountService.changePassword(login, passwordDto);
        });

        // Verify interactions
        verify(userAccountRepository, times(1)).findByLogin(login);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

    @Test
    public void testChangePassword_OldPasswordIncorrect() {
        // Mock data
        String login = "user";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";

        UserAccount userAccount = new UserAccount();
        userAccount.setLogin(login);
        userAccount.setPassword(oldPassword);

        NewPasswordDto passwordDto = new NewPasswordDto();
        passwordDto.setOldPassword(oldPassword);
        passwordDto.setNewPassword(newPassword);

        when(userAccountRepository.findByLogin(login)).thenReturn(Optional.of(userAccount));
        when(passwordEncoder.matches(oldPassword, oldPassword)).thenReturn(false);

        // Verify the exception is thrown
        assertThrows(IllegalArgumentException.class, () -> {
            userAccountService.changePassword(login, passwordDto);
        });

        // Verify interactions
        verify(userAccountRepository, times(1)).findByLogin(login);
        verify(passwordEncoder, times(1)).matches(oldPassword, oldPassword);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userAccountRepository, never()).save(any(UserAccount.class));
    }

}