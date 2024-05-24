package ait.cohort34.accounting.dto;

import ait.cohort34.accounting.model.Role;
import lombok.*;

import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String fullName;
    @Setter
    private String avatar;
    private String login;
    private String email;
    private String website;
    private String phone;
    private String telegram;
    private Set<Role> role;
}
