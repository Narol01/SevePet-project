package ait.cohort34.accounting.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class UserContactsDto {

    private String fullName;
    private String email;
    private String website;
    private String phone;
    private String telegram;
}
