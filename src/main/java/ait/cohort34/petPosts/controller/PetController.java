package ait.cohort34.petPosts.controller;

import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import ait.cohort34.petPosts.service.PetService;
import ait.cohort34.security.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pet")
public class PetController{

    final PetService petService;
    final AuthService authService;
    final ObjectMapper objectMapper;

    @PostMapping
    public PetDto addNewPet(@RequestPart("newPet") String newPet,
                            @RequestPart("photos") MultipartFile[] files) throws IOException {

        // Получение логина из аутентификационной информации
        NewPetDto newPetDto = objectMapper.readValue(newPet, NewPetDto.class);
        String login = (String) authService.getAuthInfo().getPrincipal();
        return petService.addNewPet(login, newPetDto, files);
    }

    @GetMapping("/{id}")
    public PetDto findPetById(@PathVariable Long id) {

        return petService.findPetById(id);
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable Long id) {

        byte[] photoData = petService.getPhotoById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=photo.jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(photoData);
    }

    @GetMapping("/found")
    public Iterable<PetDto> findPetsByFilter (
            @RequestParam(required = false) String petType,
            @RequestParam(required = false) String age,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String author)
    {
        return petService.findPetsByFilter(petType, age,  gender, country, category, author);
    }
    @GetMapping("/found/{type}")
    public Iterable<PetDto> findPetByType(@PathVariable String type) {

        return petService.findPetByType(type);
    }

    @GetMapping("/found/pets")
    public Iterable<PetDto> findAll() {

        return petService.findAllPets();
    }

    @PutMapping("/{id}")
    public PetDto updatePet(@PathVariable Long id,
                            @RequestPart String petDto,
                            @RequestPart(required = false) MultipartFile[] files) throws IOException {

        UpdatePetDto updatePetDto = objectMapper.readValue(petDto, UpdatePetDto.class);
        return petService.updatePet(id,updatePetDto,files);
    }

    @DeleteMapping("/{id}")
    public PetDto removePetById(@PathVariable Long id) {

        return petService.removePetById(id);
    }
}
