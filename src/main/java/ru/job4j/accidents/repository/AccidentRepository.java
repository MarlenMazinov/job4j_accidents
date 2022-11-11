package ru.job4j.accidents.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.accidents.model.Accident;

import java.util.List;

public interface AccidentRepository extends CrudRepository<Accident, Integer> {
    @Query("SELECT DISTINCT a from Accident a left join fetch a.rules ")
    List<Accident> findAll();
}