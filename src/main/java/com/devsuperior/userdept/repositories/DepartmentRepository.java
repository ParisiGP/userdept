package com.devsuperior.userdept.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.devsuperior.userdept.entities.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long>{
    
}
