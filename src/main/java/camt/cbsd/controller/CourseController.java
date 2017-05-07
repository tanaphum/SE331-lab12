package camt.cbsd.controller;

import camt.cbsd.entity.Course;
import camt.cbsd.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;

/**
 * Created by Dto on 16-Apr-17.
 */
@RestController
public class CourseController {

    CourseService courseService;

    @Autowired
    public void setCourseService(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/course")
    public List<Course> list() {
        return courseService.list();
    }


    @PostMapping("/course")
    public ResponseEntity<?> add(@RequestBody Course course) {
        courseService.add(course);
        return ResponseEntity.ok(course);

    }
}
