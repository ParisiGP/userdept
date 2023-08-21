package com.devsuperior.userdept.controllers;


import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/search")
    public ResponseEntity<?> findByIds(@RequestParam List<Long> id) {
        List<Department> results = repository.findAllById(id);
    
        if (!results.isEmpty()) {
            return ResponseEntity.ok().body(results);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Departamentos não encontrados com os IDs fornecidos.");
        }
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

@PutMapping("/{id}")
public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody Department updatedDepartment) {
    Optional<Department> optionalDepartment = repository.findById(id);

    if (optionalDepartment.isEmpty()) {
        return new ResponseEntity<>("Departamento não encontrado com o ID: " + id, HttpStatus.NOT_FOUND);
    }

    Department department = optionalDepartment.get();

    // Atualiza o campo "name" com o valor do objeto atualizado
    department.setName(updatedDepartment.getName());

    repository.save(department); // Salva as alterações no banco de dados

    return ResponseEntity.ok("Departamento atualizado com sucesso");
}

@PatchMapping("/{id}")
public ResponseEntity<?> updateDepartmentPartial(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
    Optional<Department> optionalDepartment = repository.findById(id);

    if (optionalDepartment.isEmpty()) {
        return new ResponseEntity<>("Departamento não encontrado com o ID: " + id, HttpStatus.NOT_FOUND);
    }

    Department department = optionalDepartment.get();

    // Atualiza os campos específicos com os valores do objeto de atualização
    if (updates.containsKey("name")) {
        department.setName((String) updates.get("name"));
    }

    repository.save(department); // Salva as alterações no banco de dados

    return ResponseEntity.ok("Departamento atualizado parcialmente com sucesso");
}

}
