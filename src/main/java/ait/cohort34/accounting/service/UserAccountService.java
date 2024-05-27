package ait.cohort34.accounting.service;

import ait.cohort34.accounting.dto.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserAccountService {
    UserDto register(UserRegisterDto userRegisterDto, MultipartFile image) throws IOException;

    List<UserDto> getUsers();

    UserDto getUser(String login);

    UserDto removeUser(Long id);

    UserDto updateUser(Long id, UserEditDto userEditDto,MultipartFile image) throws IOException;

    boolean changeRole(Long id);

    void changePassword(String login, NewPasswordDto passwordDto);

    String getTelegram(Long id);

    byte[] getPhotoById(Long id);
}
