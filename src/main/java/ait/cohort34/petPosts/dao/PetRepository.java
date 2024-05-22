package ait.cohort34.petPosts.dao;

import ait.cohort34.petPosts.model.Pet;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface PetRepository extends JpaRepository<Pet,Long > {
    Stream<Pet> findByPetTypeIgnoreCase(String type);
    Stream<Pet> findPetsByAuthorIgnoreCase(String author);
    @Query("select distinct p from Pet p where (:petType is null or p.petType =:petType)" +
            "and (:age is null or p.age=:age)" + "and (:gender is null or p.gender=:gender)" +
            "and (:country is null or p.country=:country)"+ "and (:category is null or p.category=:category)" +
            "and (:disability is null or p.disability=:disability)"+ "and (:author is null or p.author=:author)")
    Stream<Pet> findPetsByFilter(@Param("petType") String petType, @Param("age") String age,
                                 @Param("gender") String gender, @Param("country") String country,
                                 @Param("category") String category, @Param("disability") Boolean disability,
                                 @Param("author") String author);
}
