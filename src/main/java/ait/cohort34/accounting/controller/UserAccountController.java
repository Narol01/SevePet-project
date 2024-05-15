package ait.cohort34.accounting.controller;

import ait.cohort34.accounting.dto.*;
import ait.cohort34.accounting.service.UserAccountService;
import ait.cohort34.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping("/user/{login}")
    public UserDto removeUser(@PathVariable String login) {
        return userAccountService.removeUser(login);
    }

    @PutMapping("/user/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody UserEditDto userEditDto) {
        return userAccountService.updateUser(login, userEditDto);
    }

    @PutMapping("/user/{login}/role")
    public boolean changeRole(@PathVariable String login) {
        return userAccountService.changeRole(login);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(Principal principal, @RequestHeader("X-Password") String newPassword) {
        userAccountService.changePassword(principal.getName(), newPassword);
    }
    @GetMapping("/user/{login}/telegram")
    public String getTelegram(@PathVariable String login){
        return userAccountService.getTelegram(login);
    }
}
