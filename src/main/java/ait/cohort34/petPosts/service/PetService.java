package ait.cohort34.petPosts.service;

import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PetService {

    PetDto addNewPet(String login, NewPetDto newPetDto, MultipartFile[] files) throws IOException;

    Iterable<PetDto> findPetByType(String type);

    Iterable<PetDto> findAllPets();

    PetDto updatePet(Long  id,UpdatePetDto updatePetDto, MultipartFile[] files) throws IOException;

    PetDto removePetById(Long  id);

    Iterable<PetDto> findPetsByFilter(String petType, String age, String gender, String country, String category, String author);

    PetDto findPetById(Long id);

    byte[] getPhotoById(Long id);
}
