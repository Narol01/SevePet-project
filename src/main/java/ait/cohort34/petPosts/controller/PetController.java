package ait.cohort34.petPosts.controller;

import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import ait.cohort34.petPosts.service.PetService;
import ait.cohort34.security.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
    private static final Logger logger = LoggerFactory.getLogger(PetController.class);

    @PostMapping
    public ResponseEntity<PetDto> addNewPet(@RequestPart("newPet") String newPet,
                                            @RequestPart("photos") MultipartFile[] files) throws IOException {

        // Получение логина из аутентификационной информации
        NewPetDto newPetDto = objectMapper.readValue(newPet, NewPetDto.class);
        String login = (String) authService.getAuthInfo().getPrincipal();

        try {
            PetDto createdPet = petService.addNewPet(login, newPetDto, files);
            return ResponseEntity.ok(createdPet);
        } catch (Exception e) {
            // Логирование ошибки и возврат статуса 500
            logger.error("Error adding new pet", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
    public ResponseEntity<PetDto> updatePet(@PathVariable Long id,
                                            @RequestPart String petDto,
                                            @RequestPart(required = false) MultipartFile[] files) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UpdatePetDto updatePetDto;

        try {
            updatePetDto = objectMapper.readValue(petDto, UpdatePetDto.class);
        } catch (IOException e) {
            logger.error("Error parsing JSON", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            PetDto updatedPet = petService.updatePet(id, updatePetDto, files);
            return ResponseEntity.ok(updatedPet);
        } catch (Exception e) {
            logger.error("Error updating pet", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public PetDto removePetById(@PathVariable Long id) {

        return petService.removePetById(id);
    }
}
