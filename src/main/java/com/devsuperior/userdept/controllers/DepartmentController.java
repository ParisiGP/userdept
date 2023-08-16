package com.devsuperior.userdept.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.userdept.repositories.DepartmentRepository;
import com.devsuperior.userdept.entities.Department;
import com.devsuperior.userdept.repositories.UserRepository;
import com.devsuperior.userdept.entities.User;

@RestController
@RequestMapping(value = "/departments")
public class DepartmentController {

    @Autowired
    private DepartmentRepository repository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List findAll() {
        List<Department> result = repository.findAll();

        return result;
    }

    @GetMapping(value = "/{id}")
    public Department findById(@PathVariable Long id) {
        Department result = repository.findById(id).get();

        return result;
    }

    @PostMapping
    public Department Insert(@RequestBody Department department) {
        Department result = repository.save(department);

        return result;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
    Optional<Department> optionalDepartment = repository.findById(id);

    if (optionalDepartment.isEmpty()) {
        return new ResponseEntity<>("Departamento não encontrado com o ID: " + id, HttpStatus.NOT_FOUND);
    }

    Department department = optionalDepartment.get();
    List<User> usersInDepartment = userRepository.findByDepartment(department);

    if (!usersInDepartment.isEmpty()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Não é possível excluir um departamento que possui usuários vinculados");
    }

    repository.deleteById(id);
    return ResponseEntity.ok("Departamento deletado com sucesso");
}
}
