package ait.cohort34.accounting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDto {
    private String fullName;
    @NotEmpty(message = "Login must not be empty")
    private String login;
    @NotEmpty(message = "Password must not be empty")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit")
    private String password;
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    private String email;
    private String website;
    private String phone;
    private String telegram;
}
