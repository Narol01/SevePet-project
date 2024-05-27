package ait.cohort34.accounting.dao;

import ait.cohort34.accounting.model.PhotoUser;
import org.springframework.data.repository.CrudRepository;

public interface UserPhotoRepository extends CrudRepository<PhotoUser, Long> {
}
