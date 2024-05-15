package ait.cohort34.petPosts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NewPetDto {
    String caption;
    String petType;
    String category;
    String gender;
    String age;
    Set<String> photo;
    String country;
    String city;
    String description;
}
