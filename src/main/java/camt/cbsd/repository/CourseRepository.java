package camt.cbsd.repository;

import camt.cbsd.entity.Course;
import camt.cbsd.entity.Student;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by Dto on 07-Apr-17.
 */
public interface CourseRepository extends CrudRepository<Course,Long> {
}
