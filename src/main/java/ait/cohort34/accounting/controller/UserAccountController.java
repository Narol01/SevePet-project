package ait.cohort34.accounting.controller;

import ait.cohort34.accounting.dto.*;
import ait.cohort34.accounting.service.UserAccountService;
import ait.cohort34.petPosts.dto.NewPetDto;
import ait.cohort34.security.service.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
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

    @PostMapping
    public ResponseEntity<UserDto> register(@RequestParam("registerDto") String registerDto,@RequestParam("image") MultipartFile image) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserRegisterDto UserRegisterDto = objectMapper.readValue(registerDto, UserRegisterDto.class);
        UserDto userDto =userAccountService.register(UserRegisterDto,image);
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
    public UserDto updateUser(@PathVariable Long id, @RequestParam("editDto") String userEditDto,@RequestParam("image") MultipartFile image) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserEditDto userEditDtoDto = objectMapper.readValue(userEditDto, UserEditDto.class);
        return userAccountService.updateUser(id, userEditDtoDto,image);
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