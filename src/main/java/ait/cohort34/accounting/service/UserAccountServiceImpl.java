package ait.cohort34.accounting.service;

import ait.cohort34.accounting.dao.RoleRepository;
import ait.cohort34.accounting.dao.UserAccountRepository;
import ait.cohort34.accounting.dao.UserPhotoRepository;
import ait.cohort34.accounting.dto.*;
import ait.cohort34.accounting.dto.exceptions.UserExistsException;
import ait.cohort34.accounting.dto.exceptions.UserNotFoundException;
import ait.cohort34.accounting.model.Role;
import ait.cohort34.accounting.model.UserAccount;
import ait.cohort34.petPosts.dao.PetRepository;
import ait.cohort34.petPosts.dto.exception.PhotoNotFoundException;
import ait.cohort34.petPosts.model.Pet;
import ait.cohort34.accounting.model.PhotoUser;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserAccountServiceImpl implements UserAccountService, CommandLineRunner {

    final UserAccountRepository userAccountRepository;
    final PetRepository petRepository;
    final ModelMapper modelMapper;
    final PasswordEncoder passwordEncoder;
    @Autowired
    private EntityManager entityManager;
    final RoleRepository roleRepository;
    final UserPhotoRepository photoRepository;

    @Override
    @Transactional
    public UserDto register(UserRegisterDto userRegisterDto, MultipartFile image) throws IOException {
        if (userAccountRepository.existsByLogin(userRegisterDto.getLogin())) {
            throw new UserExistsException("A user with this login already exists.");
        }

        UserAccount userAccount = modelMapper.map(userRegisterDto, UserAccount.class);
        String password = passwordEncoder.encode(userRegisterDto.getPassword());
        userAccount.setPassword(password);

        Role userRole = roleRepository.findByTitle("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }
        userAccount.setRoles(new HashSet<>(Collections.singletonList(userRole)));

        if (image != null && !image.isEmpty()) {
            PhotoUser photo = new PhotoUser();
            photo.setName(image.getOriginalFilename());
            photo.setType(image.getContentType());
            photo.setData(image.getBytes());

            // Устанавливаем связь между фото и аккаунтом
            userAccount.setAvatar(photo);
            photo.setUserAccount(userAccount);

            // Сохраняем пользователя (каскадное сохранение сохранит и фото)
            userAccountRepository.save(userAccount);
        } else {
            userAccountRepository.save(userAccount);
        }

        UserDto userDto = modelMapper.map(userAccount, UserDto.class);
        if (userAccount.getAvatar() != null) {
            String photoUrl = "/api/account/photos/" + userAccount.getAvatar().getId();
            userDto.setPhotoUrls(photoUrl);
        }

        return userDto;
    }

    @Override
    public List<UserDto> getUsers() {
        Iterable<UserAccount> userAccounts = userAccountRepository.findAll();
        List<UserDto> users = new ArrayList<>();

        userAccounts.forEach(userAccount -> {
            if (!"admin".equals(userAccount.getLogin())) {
                UserDto userDto = modelMapper.map(userAccount, UserDto.class);
                if (userAccount.getAvatar() != null) {
                    String photoUrl = "/api/account/photos/" + userAccount.getAvatar().getId();
                    userDto.setPhotoUrls(photoUrl);
                }
                users.add(userDto);
            }
        });

        return users;
    }

    @Override
    public UserDto getUser(String login) {
        UserAccount userAccount = userAccountRepository.findByLogin(login).orElseThrow(UserNotFoundException::new);
        UserDto userDto = modelMapper.map(userAccount, UserDto.class);
        if (userAccount.getAvatar() != null) {
            String photoUrl = "/api/account/photos/" + userAccount.getAvatar().getId();
            userDto.setPhotoUrls(photoUrl);
        }
        return userDto;
    }

    @Override
    public UserContactsDto getUserContacts(String author) {
        UserAccount userAccount = userAccountRepository.findByLogin(author).orElseThrow(UserNotFoundException::new);
        return modelMapper.map(userAccount, UserContactsDto.class);
    }

    @Override
    public byte[] getPhotoById(Long id) {
        PhotoUser photoUser = photoRepository.findById(id).orElseThrow(PhotoNotFoundException::new);
        return photoUser.getData();
    }

    @Transactional
    @Override
    public UserDto removeUser(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id).orElseThrow(UserNotFoundException::new);
        userAccountRepository.delete(userAccount);

        List<Pet> petList = petRepository.findByAuthorIgnoreCase(userAccount.getUsername()).toList();

        petRepository.deleteAll(petList);
        return modelMapper.map(userAccount, UserDto.class);
    }
    @Transactional
    @Override
    public UserDto updateUser(Long id, UserEditDto userEditDto, MultipartFile image) throws IOException {
        UserAccount userAccount = userAccountRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (userEditDto.getFullName() != null) {
            userAccount.setFullName(userEditDto.getFullName());
        }
        if (userEditDto.getEmail() != null) {
            userAccount.setEmail(userEditDto.getEmail());
        }
        if (userEditDto.getPhone() != null) {
            userAccount.setPhone(userEditDto.getPhone());
        }
        if (userEditDto.getTelegram() != null) {
            userAccount.setTelegram(userEditDto.getTelegram());
        }
        if (userEditDto.getWebsite() != null) {
            userAccount.setWebsite(userEditDto.getWebsite());
        }

        if (image != null && !image.isEmpty()) {
                byte[] newPhotoData = image.getBytes();
                if (userAccount.getAvatar() != null) {
                    userAccount.getAvatar().setData(newPhotoData);
                } else {
                    PhotoUser newPhoto = new PhotoUser();
                    newPhoto.setData(newPhotoData);
                    newPhoto.setUserAccount(userAccount);
                    userAccount.setAvatar(newPhoto);
                }

        }
        userAccountRepository.save(userAccount);
        UserDto userDto = modelMapper.map(userAccount, UserDto.class);
        if (userAccount.getAvatar() != null) {
            String photoUrl = "/api/account/photos/" + userAccount.getAvatar().getId();
            userDto.setPhotoUrls(photoUrl);
        }
        return userDto;
    }

    @Transactional
    @Override
    public boolean changeRole(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id).orElseThrow(UserNotFoundException::new);
        List<Role> roles = entityManager.createQuery("SELECT r FROM Role r WHERE r.title = :title", Role.class)
                .setParameter("title", "ROLE_ADMIN")
                .getResultList();
        if (!roles.isEmpty()) {
            Role userRole = roles.get(0);
            roleRepository.save(userRole);
            userAccount.setRoles(new HashSet<>(Collections.singletonList(userRole)));
        }
        userAccountRepository.save(userAccount);
        return true;
    }

    @Override
    public void changePassword(String login, NewPasswordDto passwordDto) {
        // Найдем пользователя по login
        UserAccount user = userAccountRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        // Проверим, что старый пароль совпадает с текущим паролем пользователя
        if (!passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        // Обновим пароль пользователя на новый
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        //сохраним обновленного пользователя
        userAccountRepository.save(user);
    }


    @Override
    public String getTelegram(Long id) {
        UserAccount userAccount = userAccountRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userAccount.getTelegram();
    }

    @Override
    public void run(String... args) throws Exception {
        if (!userAccountRepository.existsByLogin("admin")) {
            String password = passwordEncoder.encode("admin");
            UserAccount userAccount = new UserAccount("admin", null, password, "","","","","");
            Role userRole = roleRepository.findByTitle("ADMIN");
            if (userRole == null) {
                userRole = new Role("ROLE_ADMIN");
                roleRepository.save(userRole);
            }
            userAccount.setRoles(new HashSet<>(Collections.singletonList(userRole)));
//            userAccount.changeRole();
            userAccountRepository.save(userAccount);
        }
    }
}
