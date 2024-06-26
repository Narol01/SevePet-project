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
    @NotEmpty(message = "FullName must not be empty")
    @Pattern(regexp = "^(?!\\s+$)[A-Za-z\\s]+$", message = "The full name must contain only letters of the Latin alphabet")
    private String fullName;
    @NotEmpty(message = "Login must not be empty")
    @Pattern(regexp = "^(?!\\s)[A-Za-z0-9!@#$%^&*()_+=:,.?-]*[A-Za-z0-9!@#$%^&*()_+=:,.?-]+$",
       message = "Login can contain only Latin letters, numbers, and special characters: ! @ # $ % ^ & * ( ) _ + = : , . ? - (no spaces)")
    private String login;
    @NotEmpty(message = "Password must not be empty")
    @Size(min = 4, max = 8, message = "Password must be at least 4-8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit")
    private String password;
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Email should be valid")
    private String email;
    private String website;
    private String phone;
    private String telegram;
}
