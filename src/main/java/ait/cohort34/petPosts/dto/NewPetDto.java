package ait.cohort34.petPosts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class NewPetDto {
    String caption;
    String petType;
    String category;
    String gender;
    String age;
    Set<String> photos;
    String country;
    String city;
    String description;
}
