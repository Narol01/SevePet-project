package ait.cohort34.petPosts.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PetDto {
    Long  id;
    String caption;
    String petType;
    String category;
    String gender;
    String age;
    Set<String> photo;
    String country;
    String city;
    String description;
    LocalDate dateCreate;
    LocalDate deadline;
}
