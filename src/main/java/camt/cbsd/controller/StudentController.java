package camt.cbsd.controller;

import camt.cbsd.entity.Student;
import camt.cbsd.services.StudentService;
import camt.cbsd.services.StudentServiceImpl;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@ConfigurationProperties(prefix = "server")
public class StudentController {
    StudentService studentService;
    String imageServerDir;
    String imageUrl;
    String baseUrl;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageServerDir(String imageServerDir) {
        this.imageServerDir = imageServerDir;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }


    @GetMapping("/student")
    public List<Student> getStudents() {

        List<Student> students = studentService.getStudents();
        return students;
    }


    @GetMapping("/student/{id}")
    public ResponseEntity<?> getStudent(@PathVariable("id") long id) {
        Student student = studentService.findById(id);
        if (student != null)
            return ResponseEntity.ok(student);
        else
            //http code 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @GetMapping("/students")
    public ResponseEntity<?> queryStudent(HttpServletRequest request, @RequestParam("search") String query) {
        List<Student> students = studentService.queryStudent(query);
        if (students != null)
            return ResponseEntity.ok(students);
        else
            //http code 204
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

    }

    @PostMapping("/student")
    public Student uploadOnlyStudent(@RequestBody Student student) {

        studentService.addStudent(student);
        return student;

    }


    @PostMapping("/student/image")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            BufferedImage img = ImageIO.read(file.getInputStream());
            String oldFilename = file.getOriginalFilename();
            String ext = FilenameUtils.getExtension(oldFilename);
            String newFilename = Integer.toString(LocalTime.now().hashCode(), 16) + Integer.toString(oldFilename.hashCode(), 16) + "." + ext;
            File targetFile = Files.createFile(Paths.get(imageServerDir + newFilename)).toFile();
            ImageIO.write(img, ext, targetFile);

            return ResponseEntity.ok(baseUrl + imageUrl + newFilename);

        } catch (NullPointerException e) {
            return ResponseEntity.status(202).build();
        }
    }


    @GetMapping("/student/images/{fileName:.+}")
    public ResponseEntity<?> getStuentImage(@PathVariable("fileName") String filename) {
        Path pathFile = Paths.get(imageServerDir + filename);

        try {
            Resource resource = new UrlResource(pathFile.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
                        .body(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
}
