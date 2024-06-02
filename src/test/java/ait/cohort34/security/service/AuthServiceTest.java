package ait.cohort34.security.service;

import ait.cohort34.accounting.dto.UserAuthDto;
import ait.cohort34.accounting.model.UserAccount;
import ait.cohort34.accounting.service.UserService;
import ait.cohort34.security.dto.TokenResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private TokenService tokenService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLogin_Success() throws AuthenticationException {
        UserAuthDto userAuthDto = new UserAuthDto("user", "password");
        UserAccount userAccount = new UserAccount();
        userAccount.setPassword("encodedPassword");

        when(userService.loadUserByUsername("user")).thenReturn(userAccount);
        when(encoder.matches("password", "encodedPassword")).thenReturn(true);
        when(tokenService.generateAccessToken(userAccount)).thenReturn("accessToken");
        when(tokenService.generateRefreshToken(userAccount)).thenReturn("refreshToken");

        TokenResponseDto tokenResponseDto = authService.login(userAuthDto);

        assertNotNull(tokenResponseDto);
        assertEquals("accessToken", tokenResponseDto.getAccessToken());
        assertEquals("refreshToken", tokenResponseDto.getRefreshToken());
    }

    @Test
    void testLogin_InvalidPassword() {
        UserAuthDto userAuthDto = new UserAuthDto("user", "wrongPassword");
        UserAccount userAccount = new UserAccount();
        userAccount.setPassword("encodedPassword");

        when(userService.loadUserByUsername("user")).thenReturn(userAccount);
        when(encoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        AuthenticationException thrown = assertThrows(
                AuthenticationException.class,
                () -> authService.login(userAuthDto),
                "Expected login to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains("Invalid username or password"));
    }
}

