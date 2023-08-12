package com.devsuperior.userdept.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.userdept.repositories.DepartmentRepository;
import com.devsuperior.userdept.entities.Department;

@RestController
@RequestMapping(value = "/departments")
public class DepartmentController {
    
    @Autowired
    private DepartmentRepository repository;

    @GetMapping 
    public List findAll(){
        List<Department> result = repository.findAll();
        
        return result;
    }

    @GetMapping(value = "/{id}")
    public Department findById(@PathVariable Long id){
        Department result = repository.findById(id).get();
        
        return result;
    }

    @PostMapping
    public Department Insert(@RequestBody Department department){
       Department result = repository.save(department);
       
       return result;
    }
}
