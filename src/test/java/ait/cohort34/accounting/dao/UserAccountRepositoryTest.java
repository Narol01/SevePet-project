package ait.cohort34.accounting.dao;

import ait.cohort34.accounting.model.UserAccount;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserAccountRepositoryTest {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void testFindByLogin_UserExists() {
        // Arrange: подготовка данных
        UserAccount userAccount = new UserAccount();
        userAccount.setLogin("testuser");
        userAccountRepository.save(userAccount);

        // Act: вызов тестируемого метода
        Optional<UserAccount> foundUser = userAccountRepository.findByLogin("testuser");

        // Assert: проверка результатов
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getLogin());
    }

    @Test
    public void testFindByLogin_UserNotExists() {
        // Act: вызов тестируемого метода
        Optional<UserAccount> foundUser = userAccountRepository.findByLogin("nonexistentuser");

        // Assert: проверка результатов
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testExistsByLogin_UserExists() {
        // Arrange: подготовка данных
        UserAccount userAccount = new UserAccount();
        userAccount.setLogin("testuser");
        userAccountRepository.save(userAccount);

        // Act: вызов тестируемого метода
        Boolean exists = userAccountRepository.existsByLogin("testuser");

        // Assert: проверка результатов
        assertTrue(exists);
    }

    @Test
    public void testExistsByLogin_UserNotExists() {
        // Act: вызов тестируемого метода
        Boolean exists = userAccountRepository.existsByLogin("nonexistentuser");

        // Assert: проверка результатов
        assertFalse(exists);
    }
}
