package ait.cohort34.accounting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEditDto {
    @NotEmpty(message = "FullName must not be empty")
    @Pattern(regexp = "^(?!\\s+$)[A-Za-z\\s]+$", message = "The full name must contain only letters of the Latin alphabet")
    private String fullName;
    private String telegram;
    @Email(message = "Email should be valid")
    private String email;
    private String website;
    private String phone;
}