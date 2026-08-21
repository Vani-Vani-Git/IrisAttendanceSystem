package com.vani.irisapi.repository;
import com.vani.irisapi.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
public interface StudentRepository extends JpaRepository<Student, String> {

}
