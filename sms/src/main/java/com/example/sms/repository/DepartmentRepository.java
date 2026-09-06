// DepartmentRepository.java
package com.example.sms.repository;
import com.example.sms.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {}