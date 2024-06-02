package ait.cohort34.accounting.dao;

import ait.cohort34.accounting.model.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindByTitle_RoleExists() {
        // Arrange: подготовка данных
        Role role = new Role();
        role.setTitle("ROLE_USER");
        roleRepository.save(role);

        // Act: вызов тестируемого метода
        Role foundRole = roleRepository.findByTitle("ROLE_USER");

        // Assert: проверка результатов
        assertNotNull(foundRole);
        assertEquals("ROLE_USER", foundRole.getTitle());
    }

    @Test
    public void testFindByTitle_RoleNotExists() {
        // Act: вызов тестируемого метода
        Role foundRole = roleRepository.findByTitle("ROLE_NON_EXISTENT");

        // Assert: проверка результатов
        assertNull(foundRole);
    }
}