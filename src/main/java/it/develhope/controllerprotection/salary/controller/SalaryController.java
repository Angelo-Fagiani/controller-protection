package it.develhope.controllerprotection.salary.controller;

import it.develhope.controllerprotection.salary.entities.CreateSalaryDTO;
import it.develhope.controllerprotection.salary.entities.Salary;
import it.develhope.controllerprotection.salary.repository.SalaryRepository;
import it.develhope.controllerprotection.user.entities.User;
import it.develhope.controllerprotection.user.repository.UserRepository;
import it.develhope.controllerprotection.user.utils.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/salary")
@PreAuthorize("hasRole('"+ Roles.REGISTERED +"')")
public class SalaryController {

    @Autowired
    SalaryRepository salaryRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    @PreAuthorize("hasRole('"+ Roles.ADMIN +"')")
    public List<Salary> getAll(){
    return salaryRepository.findAll();
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('"+ Roles.ADMIN +"')")
    public Salary getUserBySalary(@PathVariable Long id){
        return salaryRepository.findByUserId(id);
    }

    @PostMapping("/user/{id}")
    @PostAuthorize("hasRole('"+Roles.ADMIN +"')")
    public Salary createSalary(@PathVariable Long id, @RequestBody CreateSalaryDTO salaryDTO){
        Salary salary = new Salary();
        salary.setAmount(salaryDTO.getAmount());
        Optional<User> user = userRepository.findById(id);
        salary.setUser(user.get());
        return salaryRepository.save(salary);
    }


}
