package ait.cohort34.petPosts.dto;

import ait.cohort34.petPosts.model.Photo;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class PetDto {
    Long  id;
    String author;
    String caption;
    String petType;
    String category;
    String gender;
    String age;
    Set<String> photos;
    String country;
    String city;
    String description;
    private Set<String> photoUrls;
    LocalDate dateCreate;
    LocalDate deadline;
}
