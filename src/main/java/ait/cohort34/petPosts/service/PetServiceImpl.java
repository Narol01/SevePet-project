package ait.cohort34.petPosts.service;

import ait.cohort34.petPosts.dao.PetRepository;
import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import ait.cohort34.petPosts.dto.exseption.PetNotFoundException;
import ait.cohort34.petPosts.model.Pet;
import ait.cohort34.petPosts.model.Photo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {

    final ModelMapper modelMapper;
    final PetRepository petRepository;


    @Override
    public PetDto addNewPet(String login, NewPetDto newPetDto) {
        Set<String> photos = newPetDto.getPhotos();
        // Пример кода для создания и сохранения Pet с фото
        Set<Photo> photoSet = new HashSet<>();
        for (String ph : photos) {
            byte[] photoData = Base64.getDecoder().decode(ph);
            Photo photo = new Photo(photoData);
            photoSet.add(photo);
        }

        // Create the Pet entity
        Pet pet = new Pet(newPetDto.getCaption(),
                newPetDto.getPetType(),
                newPetDto.getDescription(),
                newPetDto.getCity(),
                newPetDto.getCountry(),
                photoSet,
                newPetDto.getAge(),
                newPetDto.getGender(),
                newPetDto.getCategory());
        pet.setAuthor(login);

        // Установите двустороннюю связь
        for (Photo photo : photoSet) {
            photo.setPet(pet);
        }

        petRepository.save(pet);
        return modelMapper.map(pet, PetDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<PetDto> findPetByType(String type) {
        return petRepository.findByPetTypeIgnoreCase(type)
                .map(s -> modelMapper.map(s, PetDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<PetDto> findPetsByFilter(String petType, String age, String gender, String country, String category, Boolean disability, String author) {
        return petRepository.findPetsByFilter(petType, age, gender, country, category, author)
                .map(pet -> modelMapper.map(pet, PetDto.class))
                .toList();
    }

    @Override
    public PetDto findPetById(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(PetNotFoundException::new);

        PetDto petDto = modelMapper.map(pet, PetDto.class);

        if (pet.getPhotos() != null) {
            Set<String> photoList = pet.getPhotos().stream()
                    .map(photo -> Base64.getEncoder().encodeToString(photo.getData()))
                    .collect(Collectors.toSet());
            petDto.setPhotos(photoList);
        }
        return modelMapper.map(pet, PetDto.class);
    }

    @Override
    public Iterable<PetDto> findAllPets() {

        return petRepository.findAll().stream().map(s -> modelMapper.map(s, PetDto.class)).toList();
    }

    @Transactional
    @Override
    public PetDto updatePet(Long id, UpdatePetDto updatePetDto) {
        Pet pet = petRepository.findById(id).orElseThrow(PetNotFoundException::new);
        pet.setCaption(updatePetDto.getCaption());
        pet.setCategory(updatePetDto.getCategory());
        pet.setGender(updatePetDto.getGender());
        pet.setAge(updatePetDto.getAge());
        pet.setCountry(updatePetDto.getCountry());
        pet.setCity(updatePetDto.getCity());
        pet.setDescription(updatePetDto.getDescription());
        Set<Photo> photos = updatePetDto.getPhotos().stream()
                .map(Base64.getDecoder()::decode)
                .map(Photo::new)
                .collect(Collectors.toSet());
        pet.setPhotos(photos);
        petRepository.save(pet);
        return modelMapper.map(pet, PetDto.class);
    }

    @Transactional
    @Override
    public PetDto removePetById(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(PetNotFoundException::new);
        petRepository.delete(pet);
        return modelMapper.map(pet, PetDto.class);
    }
}
