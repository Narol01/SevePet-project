package ait.cohort34.accounting.controller;

import ait.cohort34.accounting.dto.*;
import ait.cohort34.accounting.model.UserAccount;
import ait.cohort34.accounting.service.UserAccountService;
import ait.cohort34.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class UserAccountController {
    final UserAccountService userAccountService;
    final AuthService authService;

    @PostMapping
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userAccountService.register(userRegisterDto);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userAccountService.getUsers();
    }

    @GetMapping
    public UserDto getUser() {
        return userAccountService.getUser((String)authService.getAuthInfo().getPrincipal());

    }

    @DeleteMapping("/user/{id}")
    public UserDto removeUser(@PathVariable Long id) {
        return userAccountService.removeUser(id);
    }

    @PutMapping("/user/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserEditDto userEditDto) {
        return userAccountService.updateUser(id, userEditDto);
    }

    @PutMapping("/user/{id}/role")
    public boolean changeRole(@PathVariable Long id) {
        return userAccountService.changeRole(id);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Аннотация для установки статуса ответа 204
    public void changePassword(@RequestBody NewPasswordDto passwordDto) {
//  Получаем текущего аутентифицированного пользователя
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        UserAccount userAccount = (UserAccount) authentication.getPrincipal();
//        Long userId = userAccount.getId();
        String login = (String)authService.getAuthInfo().getPrincipal();
        // Вызываем сервис для изменения пароля
        userAccountService.changePassword(login, passwordDto);
    }

    @GetMapping("/user/{id}/telegram")
    public String getTelegram(@PathVariable Long id){
        return userAccountService.getTelegram(id);
    }
}
