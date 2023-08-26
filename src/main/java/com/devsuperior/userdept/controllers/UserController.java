package com.devsuperior.userdept.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.devsuperior.userdept.entities.User;
import com.devsuperior.userdept.repositories.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class UserController {

    @Autowired
    private UserRepository repository;

    @GetMapping
    public List<User> findAll() {
        List<User> result = repository.findAll();

        return result;
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> findByIds(@RequestParam List<Long> id) {
        List<User> result = repository.findAllById(id);

        if (!result.isEmpty()) {
            return ResponseEntity.ok().body(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Usuário não encontrado com o ID: " + id));
        }
    }

    @PostMapping
    public ResponseEntity<?> insertUser(@RequestBody User user) {
        try {
            // Processa o usuário e tenta salvar
            User result = repository.save(user);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Em caso de erro, retorna uma resposta genérica com código 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Erro ao salvar o usuário."));
        }
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

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User updateUser) {
        Optional<User> optionalUser = repository.findById(id);

        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>("Usuário não encontrado com o ID: " + id, HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        user.setName(updateUser.getName());
        user.setEmail(updateUser.getEmail());
        user.setDepartment(updateUser.getDepartment());

        repository.save(user);

        return ResponseEntity.ok("Usuário atualizado com sucesso");
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateUserPartially(@PathVariable Long id, @RequestBody User partialUpdate) {
        Optional<User> optionalUser = repository.findById(id);

        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(false, "Usuário não encontrado com o ID: " + id));
        }

        User user = optionalUser.get();

        if (partialUpdate.getName() != null) {
            user.setName(partialUpdate.getName());
        }
        if (partialUpdate.getEmail() != null) {
            user.setEmail(partialUpdate.getEmail());
        }
        if (partialUpdate.getDepartment() != null) {
            user.setDepartment(partialUpdate.getDepartment());
        }

        try {
            repository.save(user);

            // Resposta padrão em formato JSON
            String successMessage = "Usuário atualizado com sucesso";
            return ResponseEntity.ok().body(new ApiResponse(true, successMessage));
        } catch (Exception e) {
            // Em caso de erro, retorna uma resposta genérica com código 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Editar usuário falhou."));
        }

    }

    static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
