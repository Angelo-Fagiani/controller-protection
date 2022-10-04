package it.develhope.controllerprotection.salary.repository;

import it.develhope.controllerprotection.salary.entities.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    Salary findByUserId(Long id);
}
