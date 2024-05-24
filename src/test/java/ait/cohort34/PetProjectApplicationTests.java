package ait.cohort34;

import ait.cohort34.accounting.service.UserAccountServiceImpl;
import ait.cohort34.petPosts.dao.PetRepository;
import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import ait.cohort34.petPosts.model.Pet;

import ait.cohort34.petPosts.service.PetServiceImpl;
import ait.cohort34.accounting.dao.RoleRepository;
import ait.cohort34.accounting.dao.UserAccountRepository;

import ait.cohort34.accounting.dto.UserDto;
import ait.cohort34.accounting.dto.UserEditDto;
import ait.cohort34.accounting.dto.UserRegisterDto;
import ait.cohort34.accounting.model.UserAccount;
import ait.cohort34.accounting.model.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PetProjectApplicationTests {

    @Mock
    private PetRepository petRepository;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EntityManager entityManager;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PetServiceImpl petService;

    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void contextLoads() {
    }

    // PetServiceImpl tests

    @Test
    void testAddNewPet() {
        NewPetDto newPetDto = new NewPetDto();
        newPetDto.setCaption("Test Caption");
        newPetDto.setPetType("Dog");
        newPetDto.setDescription("Test Description");
        newPetDto.setCity("Test City");
        newPetDto.setCountry("Test Country");
        newPetDto.setAge("2");
        newPetDto.setGender("Male");
        newPetDto.setCategory("Test Category");
        newPetDto.setPhoto(Collections.emptySet());

        Pet pet = new Pet();
        pet.setCaption("Test Caption");

        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(modelMapper.map(any(Pet.class), eq(PetDto.class))).thenReturn(new PetDto());

        PetDto petDto = petService.addNewPet("testUser", newPetDto);

        assertNotNull(petDto);
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void testFindPetByType() {
        List<Pet> pets = new ArrayList<>();
        when(petRepository.findByPetTypeIgnoreCase(anyString())).thenReturn(pets.stream());

        Iterable<PetDto> petDtos = petService.findPetByType("Dog");

        assertNotNull(petDtos);
        verify(petRepository, times(1)).findByPetTypeIgnoreCase(anyString());
    }

    @Test
    void testUpdatePet() {
        UpdatePetDto updatePetDto = new UpdatePetDto();
        updatePetDto.setCaption("Updated Caption");

        Pet pet = new Pet();
        pet.setCaption("Old Caption");

        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));
        when(petRepository.save(any(Pet.class))).thenReturn(pet);
        when(modelMapper.map(any(Pet.class), eq(PetDto.class))).thenReturn(new PetDto());

        PetDto petDto = petService.updatePet(1L, updatePetDto);

        assertNotNull(petDto);
        verify(petRepository, times(1)).findById(anyLong());
        verify(petRepository, times(1)).save(any(Pet.class));
    }

    @Test
    void testRemovePetById() {
        Pet pet = new Pet();

        when(petRepository.findById(anyLong())).thenReturn(Optional.of(pet));
        doNothing().when(petRepository).delete(any(Pet.class));
        when(modelMapper.map(any(Pet.class), eq(PetDto.class))).thenReturn(new PetDto());

        PetDto petDto = petService.removePetById(1L);

        assertNotNull(petDto);
        verify(petRepository, times(1)).findById(anyLong());
        verify(petRepository, times(1)).delete(any(Pet.class));
    }

    // UserAccountServiceImpl tests

    @Test
    void testRegister() {
        UserRegisterDto userRegisterDto = new UserRegisterDto();
        userRegisterDto.setLogin("testUser");
        userRegisterDto.setPassword("password");

        UserAccount userAccount = new UserAccount();
        userAccount.setLogin("testUser");

        when(userAccountRepository.existsByLogin(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findByTitle(anyString())).thenReturn(null);
        when(roleRepository.save(any(Role.class))).thenReturn(new Role("ROLE_USER"));
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);
        when(modelMapper.map(any(UserAccount.class), eq(UserDto.class))).thenReturn(new UserDto());

        UserDto userDto = userAccountService.register(userRegisterDto);

        assertNotNull(userDto);
        verify(userAccountRepository, times(1)).existsByLogin(anyString());
        verify(passwordEncoder, times(1)).encode(anyString());
        verify(roleRepository, times(1)).findByTitle(anyString());
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    void testGetUser() {
        UserAccount userAccount = new UserAccount();
        userAccount.setLogin("testUser");

        when(userAccountRepository.findByLogin(anyString())).thenReturn(Optional.of(userAccount));
        when(modelMapper.map(any(UserAccount.class), eq(UserDto.class))).thenReturn(new UserDto());

        UserDto userDto = userAccountService.getUser("testUser");

        assertNotNull(userDto);
        verify(userAccountRepository, times(1)).findByLogin(anyString());
    }

    @Test
    void testRemoveUser() {
        UserAccount userAccount = new UserAccount();
        userAccount.setLogin("testUser");

        // Создаем пустой список Pet для возврата из findByAuthorIgnoreCase
        List<Pet> pets = Collections.emptyList();

        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.of(userAccount));
        doNothing().when(userAccountRepository).delete(any(UserAccount.class));
        // Исправляем возврат пустого потока
        when(petRepository.findByAuthorIgnoreCase(anyString())).thenReturn(pets.stream());
        doNothing().when(petRepository).deleteAll(anyList());
        when(modelMapper.map(any(UserAccount.class), eq(UserDto.class))).thenReturn(new UserDto());

        UserDto userDto = userAccountService.removeUser(1L);

        assertNotNull(userDto);
        verify(userAccountRepository, times(1)).findById(anyLong());
        verify(userAccountRepository, times(1)).delete(any(UserAccount.class));
        verify(petRepository, times(1)).findByAuthorIgnoreCase(anyString());
        verify(petRepository, times(1)).deleteAll(anyList());
    }

    @Test
    void testUpdateUser() {
        UserEditDto userEditDto = new UserEditDto();
        userEditDto.setFullName("Updated Name");

        UserAccount userAccount = new UserAccount();

        when(userAccountRepository.findById(anyLong())).thenReturn(Optional.of(userAccount));
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);
        when(modelMapper.map(any(UserAccount.class), eq(UserDto.class))).thenReturn(new UserDto());

        UserDto userDto = userAccountService.updateUser(1L, userEditDto);

        assertNotNull(userDto);
        verify(userAccountRepository, times(1)).findById(anyLong());
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }
}
