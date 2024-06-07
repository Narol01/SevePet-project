package ait.cohort34.accounting.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPasswordDto {

    String oldPassword;
    @NotEmpty(message = "Password must not be empty")
    @Size(min = 4, max = 8, message = "Password must be at least 4-8 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).+$", message = "Password must contain at least one uppercase letter, one lowercase letter, and one digit")
    String newPassword;

}
