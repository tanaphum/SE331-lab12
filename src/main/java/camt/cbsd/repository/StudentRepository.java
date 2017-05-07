package camt.cbsd.repository;

import camt.cbsd.entity.Student;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by Dto on 07-Apr-17.
 */
public interface StudentRepository extends CrudRepository <Student,Long> {
    Student findById(Long id);
    Student findByUserUsername(String username);
    List<Student> findByName(String name);
    List<Student> findByNameIgnoreCase(String name);
    List<Student> findByNameIgnoreCaseContaining(String name);
    List<Student> findByNameIgnoreCaseContainingOrderByName(String name);
    List<Student> findBySurnameIgnoreCaseContaining(String surname);
    List<Student> findByNameIgnoreCaseContainingOrSurnameIgnoreCaseContaining(String name,String surname);
}
