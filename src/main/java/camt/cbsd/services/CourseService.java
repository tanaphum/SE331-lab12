package camt.cbsd.services;

import camt.cbsd.entity.Course;

import java.util.List;

/**
 * Created by Dto on 16-Apr-17.
 */
public interface CourseService {
    List<Course> list();
    Course add(Course course);
}
