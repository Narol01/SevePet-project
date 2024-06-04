package ait.cohort34.petPosts.dao;

import ait.cohort34.petPosts.model.Pet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    private Pet pet1;
    private Pet pet2;
    private Pet pet3;

    @BeforeEach
    public void setUp() {
        pet1 = new Pet();
        pet1.setCaption("Pet 1");
        pet1.setAuthor("author1");
        pet1.setPetType("Dog");
        pet1.setCategory("Medium");
        pet1.setGender("Male");
        pet1.setAge("2 years");
        pet1.setCountry("USA");
        pet1.setCity("New York");
        pet1.setDescription("Description 1");

        pet2 = new Pet();
        pet2.setCaption("Pet 2");
        pet2.setAuthor("author2");
        pet2.setPetType("Cat");
        pet2.setCategory("Small");
        pet2.setGender("Female");
        pet2.setAge("1 year");
        pet2.setCountry("Canada");
        pet2.setCity("Toronto");
        pet2.setDescription("Description 2");

        pet3 = new Pet();
        pet3.setCaption("Pet 3");
        pet3.setAuthor("author1");
        pet3.setPetType("Dog");
        pet3.setCategory("Large");
        pet3.setGender("Female");
        pet3.setAge("3 years");
        pet3.setCountry("USA");
        pet3.setCity("Los Angeles");
        pet3.setDescription("Description 3");

        petRepository.save(pet1);
        petRepository.save(pet2);
        petRepository.save(pet3);
    }

    @Test
    public void testFindByPetTypeIgnoreCase() {
        List<Pet> dogs = petRepository.findByPetTypeIgnoreCase("dog").collect(Collectors.toList());
        assertEquals(2, dogs.size());
        assertTrue(dogs.stream().anyMatch(pet -> pet.getCaption().equals("Pet 1")));
        assertTrue(dogs.stream().anyMatch(pet -> pet.getCaption().equals("Pet 3")));
    }

    @Test
    public void testFindByAuthorIgnoreCase() {
        List<Pet> author1Pets = petRepository.findByAuthorIgnoreCase("author1").collect(Collectors.toList());
        assertEquals(2, author1Pets.size());
        assertTrue(author1Pets.stream().anyMatch(pet -> pet.getCaption().equals("Pet 1")));
        assertTrue(author1Pets.stream().anyMatch(pet -> pet.getCaption().equals("Pet 3")));
    }

    @Test
    public void testFindPetsByFilter() {
        List<Pet> filteredPets = petRepository.findPetsByFilter("Dog", null, null, "USA", null, null)
                .collect(Collectors.toList());
        assertEquals(2, filteredPets.size());
        assertTrue(filteredPets.stream().anyMatch(pet -> pet.getCaption().equals("Pet 1")));
        assertTrue(filteredPets.stream().anyMatch(pet -> pet.getCaption().equals("Pet 3")));

        filteredPets = petRepository.findPetsByFilter(null, "1 year", "Female", null, null, null)
                .collect(Collectors.toList());
        assertEquals(1, filteredPets.size());
        assertTrue(filteredPets.stream().anyMatch(pet -> pet.getCaption().equals("Pet 2")));
    }
}