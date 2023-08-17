package com.devsuperior.userdept.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devsuperior.userdept.entities.User;
import com.devsuperior.userdept.repositories.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserRepository repository;
    
    @GetMapping
    public List<User> findAll(){
       List<User> result = repository.findAll();
       
       return result;
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> findByIds(@RequestParam List<Long> id){
       List<User> result = repository.findAllById(id);
       
       if(!result.isEmpty()){
           return ResponseEntity.ok().body(result);
       } else {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado com o ID: " + id);
       }
    }
    
    @PostMapping
    public User Insert(@RequestBody User user){
       User result = repository.save(user);
       
       return result;
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        Optional<User> optionalUser = repository.findById(id);
    
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado com o ID: " + id, HttpStatus.NOT_FOUND);
        }
    
        repository.deleteById(id);
        return ResponseEntity.ok("Usuário deletado com sucesso");
    }
}  
