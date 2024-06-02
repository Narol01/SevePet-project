package ait.cohort34.petPosts.service;

import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import ait.cohort34.petPosts.model.Pet;
import ait.cohort34.petPosts.model.Photo;
import ait.cohort34.petPosts.dao.PetRepository;
import ait.cohort34.petPosts.dao.PhotoRepository;
import ait.cohort34.petPosts.service.PetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PetServiceImplTest {

    @Mock
    private PetRepository petRepository;

    @Mock
    private PhotoRepository photoRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PetServiceImpl petService;

    private Pet pet;
    private PetDto petDto;
    private NewPetDto newPetDto;
    private UpdatePetDto updatePetDto;
    private MultipartFile[] files;

    @BeforeEach
    public void setUp() {

        photoRepository = Mockito.mock(PhotoRepository.class);

        pet = new Pet();
        pet.setId(1L);
        pet.setCaption("Test Pet");
        pet.setAuthor("testAuthor");
        pet.setPetType("Cat");
        pet.setCategory("Small");
        pet.setGender("Male");
        pet.setAge("2 years");
        pet.setCountry("USA");
        pet.setCity("New York");
        pet.setDescription("Test Description");

        petDto = new PetDto();
        petDto.setId(1L);
        petDto.setCaption("Test Pet");
        petDto.setAuthor("testAuthor");
        petDto.setPetType("Cat");
        petDto.setCategory("Small");
        petDto.setGender("Male");
        petDto.setAge("2 years");
        petDto.setCountry("USA");
        petDto.setCity("New York");
        petDto.setDescription("Test Description");

        newPetDto = new NewPetDto();
        newPetDto.setCaption("New Pet");
        newPetDto.setPetType("Dog");
        newPetDto.setCategory("Medium");
        newPetDto.setGender("Female");
        newPetDto.setAge("1 year");
        newPetDto.setCountry("Canada");
        newPetDto.setCity("Toronto");
        newPetDto.setDescription("New Description");

        updatePetDto = new UpdatePetDto();
        updatePetDto.setCaption("Updated Pet");
        updatePetDto.setCategory("Large");
        updatePetDto.setGender("Male");
        updatePetDto.setAge("3 years");
        updatePetDto.setCountry("Canada");
        updatePetDto.setCity("Toronto");
        updatePetDto.setDescription("Updated Description");

        files = new MultipartFile[]{
                new MockMultipartFile("file1", "file1.jpg", "image/jpeg", "test data".getBytes()),
                new MockMultipartFile("file2", "file2.png", "image/png", "test data".getBytes())
        };
    }

//    @Test
//    public void testAddNewPet() throws IOException {
//        when(petRepository.save(any(Pet.class))).thenReturn(pet);
//        when(photoRepository.save(any(Photo.class))).thenAnswer(i -> i.getArgument(0));
//        when(modelMapper.map(any(Pet.class), any())).thenReturn(petDto);
//
//        PetDto result = petService.addNewPet("testAuthor", newPetDto, files);
//
//        assertNotNull(result);
//        assertEquals("Test Pet", result.getCaption());
//        assertEquals("Cat", result.getPetType());
//    }

    @Test
    public void testFindPetById() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(modelMapper.map(any(Pet.class), any())).thenReturn(petDto);

        PetDto result = petService.findPetById(1L);

        assertNotNull(result);
        assertEquals("Test Pet", result.getCaption());
        assertEquals("Cat", result.getPetType());
    }

//    @Test
//    public void testUpdatePet() throws IOException {
//        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
//        when(petRepository.save(any(Pet.class))).thenReturn(pet);
//        when(photoRepository.save(any(Photo.class))).thenAnswer(i -> i.getArgument(0));
//        when(modelMapper.map(any(Pet.class), any())).thenReturn(petDto);
//
//        PetDto result = petService.updatePet(1L, updatePetDto, files);
//
//        //assertNotNull(result);
//        assertEquals("Test Pet", result.getCaption());
//        assertEquals("Cat", result.getPetType());
//    }

    @Test
    public void testRemovePetById() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet));
        when(modelMapper.map(any(Pet.class), any())).thenReturn(petDto);

        PetDto result = petService.removePetById(1L);

        assertNotNull(result);
        assertEquals("Test Pet", result.getCaption());
        assertEquals("Cat", result.getPetType());
    }
}