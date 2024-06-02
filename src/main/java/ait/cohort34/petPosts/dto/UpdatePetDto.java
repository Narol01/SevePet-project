package ait.cohort34.petPosts.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class UpdatePetDto {
    String caption;
    String category;
    String gender;
    String age;
    //Set<String> photos;
    String country;
    String city;
    String description;
}
