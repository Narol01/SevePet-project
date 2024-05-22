package ait.cohort34.security.service;

import ait.cohort34.petPosts.dao.PetRepository;
import ait.cohort34.petPosts.model.Pet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class CustomWebSecurity {

        final PetRepository petRepository;

        public boolean checkPetAuthor(Long  petId, String userName){
            Pet pet = petRepository.findById(petId).orElse(null);//нет смысла бросать здесь ошибку - она будет 500-ой
            return pet !=null && pet.getAuthor().equals(userName);
        }
    }

