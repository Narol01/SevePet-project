package ait.cohort34.accounting.dao;

import ait.cohort34.accounting.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByTitle(String title);
}
