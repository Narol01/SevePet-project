package ait.cohort34.petPosts.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePetDto {
    String caption;
    String type;// в аптейд не нужно
    String category;
    String breed;// не нужно
    String gender;
    String age;
    Boolean disability;
    Set<String> photo;
    String country;
    String city;
    String description;
    String firma;// не нужно
}
