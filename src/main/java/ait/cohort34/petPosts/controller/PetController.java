package ait.cohort34.petPosts.controller;

import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.petPosts.dto.PetDto;
import ait.cohort34.petPosts.dto.UpdatePetDto;
import ait.cohort34.petPosts.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pet")
public class PetController{
    @Autowired
    final PetService petService;

    @PostMapping("/{author}")
    public PetDto addNewPet(@PathVariable String author,@RequestBody NewPetDto newPetDto) {//сделать запрос по токену
        return petService.addNewPet(author,newPetDto);
        // в дальнейшем при создании поста будет передаваться принципал логин что упростит отправку запроса
    }
    @GetMapping("/found/caption/{caption}")
    public PetDto findPetByCaption(@PathVariable String caption) {
        return petService.findPetByCaption(caption);
    }
    @GetMapping("/found/age/{age}")
    public Iterable<PetDto> findPetsByAge(@PathVariable String age) {
        return petService.findPetsByAge(age);
    }
    @GetMapping("/found/gender/{gender}")
    public Iterable<PetDto> findPetsByGender(@PathVariable String gender) {
        return petService.findPetsByGender(gender);
    }
    @GetMapping("/found/country/{country}")
    public Iterable<PetDto> findPetsByCountry(@PathVariable String country) {
        return petService.findPetsByCountry(country);
    }
    @GetMapping("/found/category/{category}")
    public Iterable<PetDto> findPetsByCategory(@PathVariable String category) {
        return petService.findPetsByCategory(category);
    }
    @GetMapping("/found/disability/{disability}")
    public Iterable<PetDto> findPetsByDisability(@PathVariable Boolean disability) {
        return petService.findPetsByDisability(disability);
    }
    @GetMapping("/found/author/{author}")
    public Iterable<PetDto> findPetsByAuthor(@PathVariable String author) {
        return petService.findPetsByAuthor(author);
    }
    @GetMapping("/found")
    public Iterable<PetDto> findAll() {
        return petService.findAllPets();
    }
    @PutMapping("/update/{id}")
    public PetDto updatePet(@PathVariable String id,@RequestBody UpdatePetDto updatePetDto) {
        return petService.updatePet(id,updatePetDto);
    }

    @PutMapping("/addTime/{id}")
    public PetDto updatePetTime(@PathVariable String id) {
        return petService.plusDeadLine(id);
    }

    @DeleteMapping("/{caption}")
    public PetDto removePetByCaption(@PathVariable String caption) {
        return petService.removePetByCaption(caption);
    }
}
