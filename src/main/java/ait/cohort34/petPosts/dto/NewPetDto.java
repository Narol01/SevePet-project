package ait.cohort34.petPosts.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewPetDto {
    private String caption;
    private String petType;
    private String category;
    private String gender;
    private String age;
    private String country;
    private String city;
    private String description;
}
