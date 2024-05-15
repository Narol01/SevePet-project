package ait.cohort34.petPosts.controller;

import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import ait.cohort34.petPosts.service.PetService;
import ait.cohort34.security.service.AuthService;
import ait.cohort34.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pet")
public class PetController{
    @Autowired
    final PetService petService;
    final AuthService authService;

    @PostMapping
    public PetDto addNewPet(@RequestBody NewPetDto newPetDto) {//сделать запрос по токену
        System.out.println((String)authService.getAuthInfo().getPrincipal());
        return petService.addNewPet((String)authService.getAuthInfo().getPrincipal(),newPetDto);
        // в дальнейшем при создании поста будет передаваться принципал логин что упростит отправку запроса
    }
    @GetMapping("/{id}")
    public PetDto findPetById(@PathVariable Long id) {
        return petService.findPetById(id);
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
