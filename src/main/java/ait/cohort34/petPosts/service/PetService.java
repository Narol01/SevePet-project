package ait.cohort34.petPosts.service;

import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;

public interface PetService {
    PetDto addNewPet(String author,NewPetDto newPetDto);
    PetDto findPetByCaption(String caption);
    Iterable<PetDto> findPetsByAge(String age);
    Iterable<PetDto> findPetsByGender(String gender);
    Iterable<PetDto> findPetsByCountry(String country);
    Iterable<PetDto> findPetsByCategory(String category);
    Iterable<PetDto> findPetsByDisability(Boolean disability);
    Iterable<PetDto> findPetsByAuthor(String author);
    Iterable<PetDto> findAllPets();
    PetDto updatePet(String id, UpdatePetDto updatePetDto);
    PetDto plusDeadLine(String id);
    PetDto removePetByCaption(String caption);
}
