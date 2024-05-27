package ait.cohort34.petPosts.service;

import ait.cohort34.petPosts.dao.PetRepository;
import ait.cohort34.petPosts.dao.PhotoRepository;
import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import ait.cohort34.petPosts.dto.exseption.PetNotFoundException;
import ait.cohort34.petPosts.dto.exseption.PhotoNotFoundException;
import ait.cohort34.petPosts.model.Pet;
import ait.cohort34.petPosts.model.Photo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PetServiceImpl implements PetService {
    final ModelMapper modelMapper;
    @Autowired
    final PetRepository petRepository;
    @Autowired
    private PhotoRepository photoRepository;


    @Override
    public PetDto addNewPet(String login, NewPetDto newPetDto, MultipartFile[] files) throws IOException {
        // Преобразование файлов в сущности Photo
        Set<Photo> photoSet = new HashSet<>();
        for (MultipartFile file : files) {
            Photo photo = new Photo();
            photo.setName(file.getOriginalFilename());
            photo.setType(file.getContentType());
            photo.setData(file.getBytes());
            photoSet.add(photo);
        }
        Pet pet = new Pet();
        pet.setCaption(newPetDto.getCaption());
        pet.setPetType(newPetDto.getPetType());
        pet.setDescription(newPetDto.getDescription());
        pet.setCity(newPetDto.getCity());
        pet.setCountry(newPetDto.getCountry());
        pet.setPhotos(photoSet);
        pet.setAge(newPetDto.getAge());
        pet.setGender(newPetDto.getGender());
        pet.setCategory(newPetDto.getCategory());
        pet.setAuthor(login);

        petRepository.save(pet);
        for (Photo photo : photoSet) {
            photo.setPet(pet);
            photoRepository.save(photo);
        }

        PetDto petDto = modelMapper.map(pet, PetDto.class);
        Set<String> photoUrls = photoSet.stream()
                .map(photo -> "/api/pets/photos/" + photo.getId())
                .collect(Collectors.toSet());
        petDto.setPhotoUrls(photoUrls);

        return petDto;
    }

    public byte[] getPhotoById(Long photoId) {
        Photo photo = photoRepository.findById(photoId).orElseThrow(PhotoNotFoundException::new);
        return photo.getData();
    }

    @Transactional(readOnly = true)
    @Override
    public Iterable<PetDto> findPetByType(String type) {
        List<Pet> petByType=petRepository.findByPetTypeIgnoreCase(type).toList();
        for (Pet pet : petByType) {
            if(LocalDate.now().isAfter(pet.getDeadline())){
                petByType.remove(pet);
                petRepository.delete(pet);

            }
        }
        return petByType.stream()
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
            Set<String> photoUrls = pet.getPhotos().stream()
                    .map(photo -> "/api/pets/photos/" + photo.getId())
                    .collect(Collectors.toSet());
            petDto.setPhotoUrls(photoUrls);
        }

        return petDto;
    }

    @Override
    public Iterable<PetDto> findAllPets() {

        return petRepository.findAll().stream().map(s -> modelMapper.map(s, PetDto.class)).toList();
    }

    @Transactional
    @Override
    public PetDto updatePet(Long id, UpdatePetDto updatePetDto, MultipartFile[] files) throws IOException {
        Pet pet = petRepository.findById(id).orElseThrow(PetNotFoundException::new);
        pet.setCaption(updatePetDto.getCaption());
        pet.setCategory(updatePetDto.getCategory());
        pet.setGender(updatePetDto.getGender());
        pet.setAge(updatePetDto.getAge());
        pet.setCountry(updatePetDto.getCountry());
        pet.setCity(updatePetDto.getCity());
        pet.setDescription(updatePetDto.getDescription());
        Set<Photo> photoSet = new HashSet<>();
        if (files != null) {
            for (MultipartFile file : files) {
                Photo photo = new Photo();
                photo.setName(file.getOriginalFilename());
                photo.setType(file.getContentType());
                photo.setData(file.getBytes());
                photo.setPet(pet); // Устанавливаем связь с питомцем
                photoSet.add(photo);
            }
        }
        if (!photoSet.isEmpty()) {

            Set<Photo> existingPhotos = pet.getPhotos();
            photoRepository.deleteAll(existingPhotos);
            pet.setPhotos(photoSet);
            photoRepository.saveAll(photoSet);
        }
        petRepository.save(pet);
        PetDto petDto = modelMapper.map(pet, PetDto.class);
        Set<String> photoUrls = pet.getPhotos().stream()
                .map(photo -> "/api/pets/photos/" + photo.getId())
                .collect(Collectors.toSet());
        petDto.setPhotoUrls(photoUrls);
        return petDto;
    }

    @Transactional
    @Override
    public PetDto removePetById(Long id) {
        Pet pet = petRepository.findById(id).orElseThrow(PetNotFoundException::new);
        petRepository.delete(pet);
        return modelMapper.map(pet, PetDto.class);
    }
}
