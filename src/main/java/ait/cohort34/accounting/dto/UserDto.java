package ait.cohort34.accounting.dto;

import ait.cohort34.accounting.model.Role;
import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class UserDto {
    private Long id;
    private String fullName;
    private String avatar;
    private String login;
    private String email;
    private String website;
    private String phone;
    private String telegram;
    private String photoUrls;
    private Set<Role> role;
}
