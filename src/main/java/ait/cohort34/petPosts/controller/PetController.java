package ait.cohort34.petPosts.controller;

import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import ait.cohort34.petPosts.service.PetService;
import ait.cohort34.security.service.AuthService;
import ait.cohort34.security.service.TokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    final PetService petService;
    @Autowired
    final AuthService authService;

    @PostMapping
    public PetDto addNewPet(@RequestPart("newPet") String newPet,
                            @RequestPart("photos") MultipartFile[] files) throws IOException {
        // Получение логина из аутентификационной информации
        ObjectMapper objectMapper = new ObjectMapper();
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
            @RequestParam(required = false) Boolean disability,
            @RequestParam(required = false) String author)
    {
        return petService.findPetsByFilter(petType, age,  gender, country, category,  disability, author);
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
    public PetDto updatePet(@PathVariable Long  id,@RequestBody UpdatePetDto updatePetDto) {
        return petService.updatePet(id,updatePetDto);
    }

    @DeleteMapping("/{id}")
    public PetDto removePetByCaption(@PathVariable Long  id) {
        return petService.removePetById(id);
    }
}
