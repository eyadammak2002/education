package eya.tansik.education.repository;

import eya.tansik.education.entities.Student;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    Student findByCode(String code);
    List<Student> findByProgramId(String programId);
}
