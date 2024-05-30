package ait.cohort34.accounting.controller;

import ait.cohort34.accounting.dto.*;
import ait.cohort34.accounting.service.UserAccountService;
import ait.cohort34.security.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class UserAccountController {

    final UserAccountService userAccountService;
    final AuthService authService;
    final ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(UserAccountController.class);

    @PostMapping
    public ResponseEntity<UserDto> register(
            @RequestPart("registerDto") String registerDto,
            @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        UserRegisterDto userRegisterDto = objectMapper.readValue(registerDto, UserRegisterDto.class);
        UserDto userDto =userAccountService.register(userRegisterDto, image);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @GetMapping("/photos/{id}")
    public ResponseEntity<byte[]> getPhotoById(@PathVariable Long id) {
        byte[] photoData = userAccountService.getPhotoById(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=photo.jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(photoData);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userAccountService.getUsers();
    }

    @GetMapping
    public UserDto getUser() {
        return userAccountService.getUser((String) authService.getAuthInfo().getPrincipal());
    }

    @GetMapping("/{author}")
    public UserDto getUser(@PathVariable String author) {
        return userAccountService.getUser(author);
    }

    @DeleteMapping("/user/{id}")
    public UserDto removeUser(@PathVariable Long id) {
        return userAccountService.removeUser(id);
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable Long id,
            @RequestPart("editDto") String userEditDto,
            @RequestPart(value = "image",required = false) MultipartFile image) throws IOException {

        UserEditDto userEditDtoDto = objectMapper.readValue(userEditDto, UserEditDto.class);
        UserDto updatedUser = userAccountService.updateUser(id, userEditDtoDto, image);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/user/{id}/role")
    public boolean changeRole(@PathVariable Long id) {
        return userAccountService.changeRole(id);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Аннотация для установки статуса ответа 204
    public void changePassword(@RequestBody NewPasswordDto passwordDto) {
        String login = (String)authService.getAuthInfo().getPrincipal();
        // Вызываем сервис для изменения пароля
        userAccountService.changePassword(login, passwordDto);
    }

    @GetMapping("/user/{id}/telegram")
    public String getTelegram(@PathVariable Long id) {
        return userAccountService.getTelegram(id);
    }
}
//  Получаем текущего аутентифицированного пользователя
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
//        Long userId = userAccount.getId();