package ait.cohort34.accounting.service;

import ait.cohort34.accounting.dto.*;

import java.util.List;

public interface UserAccountService {
    UserDto register(UserRegisterDto userRegisterDto);

    List<UserDto> getUsers();

    UserDto getUser(String login);

    UserDto removeUser(Long id);

    UserDto updateUser(Long id, UserEditDto userEditDto);

    boolean changeRole(Long id);

    void changePassword(String login, NewPasswordDto passwordDto);

    String getTelegram(Long id);
}
