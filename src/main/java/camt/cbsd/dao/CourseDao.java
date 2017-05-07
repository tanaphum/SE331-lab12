package camt.cbsd.dao;

import camt.cbsd.entity.Course;

import java.util.List;

/**
 * Created by Dto on 07-Apr-17.
 */
public interface CourseDao {
    Course add(Course course);
    List<Course> list();
}
