package com.devsuperior.userdept.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.userdept.entities.Department;
import com.devsuperior.userdept.entities.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>{
    List<User> findByDepartment(Department department);
}


