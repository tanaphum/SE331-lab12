package camt.cbsd.services;

import camt.cbsd.dao.StudentDao;
import camt.cbsd.entity.RegisterEntity;
import camt.cbsd.entity.Student;
import camt.cbsd.entity.security.Authority;
import camt.cbsd.entity.security.AuthorityName;
import camt.cbsd.entity.security.User;
import camt.cbsd.security.repository.AuthorityRepository;
import camt.cbsd.security.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;



@Service
@ConfigurationProperties(prefix = "server")
public class StudentServiceImpl implements StudentService {
    @Autowired
    StudentDao studentDao;


    String imageServerDir;

    public void setImageServerDir(String imageServerDir) {
        this.imageServerDir = imageServerDir;
    }

    @Transactional
    public List<Student> getStudents(){
        List<Student> students = studentDao.getStudents();
        for(Student student:students){
            Hibernate.initialize(student.getEnrolledCourse());
        }
        return students;
    }

    @Override
    @Transactional
    public Student findById(long id) {
        Student student = studentDao.findById(id);
        Hibernate.initialize(student.getEnrolledCourse());
        return student;
    }

    @Override
    public Student addStudent(Student student) {
        return studentDao.addStudent(student);
    }

    @Transactional
    @Override
    public Student getStudentForTransfer(String username) {
        Student student = studentDao.findByUsername(username);
        Hibernate.initialize(student.getUser());
        Hibernate.initialize(student.getAuthorities());
        return student;
    }

    @Override
    public Student addStudent(Student student, String imageFileName, BufferedImage image) throws IOException {
        // save file to the server
        int newId = studentDao.size()+1;
        String newFilename = newId +"."+ imageFileName;
        File targetFile = Files.createFile(Paths.get(imageServerDir+newFilename)).toFile();
        ImageIO.write(image,FilenameUtils.getExtension(imageFileName),targetFile);

        student.setImage(newFilename);
        studentDao.addStudent(student);
        return student;
    }

    @Override
    public List<Student> queryStudent(String query) {
        if (query == null || query.equals(""))
            return studentDao.getStudents();
        return studentDao.getStudents(query);
    }

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    UserRepository userRepository;

    @Transactional
    @Override
    public Student addStudent(RegisterEntity registerEntity) {
        Authority authority;
        if (registerEntity.getRole().equals("Admin")){
            authority =
                    authorityRepository.findByName(AuthorityName.ROLE_ADMIN);
        }else{
            authority =
                    authorityRepository.findByName(AuthorityName.ROLE_USER);
        }
        Student student = registerEntity.getStudent();
        User user = User.builder().username(registerEntity.getUsername())
                .password(registerEntity.getPassword())
                .firstname(student.getName())
                .lastname("default surnmae")
                .email("default @default")

                .lastPasswordResetDate(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()))
                .authorities(Arrays.asList(authority))
                .enabled(true)
                .build();
        student = studentDao.addStudent(student);
        user = userRepository.save(user);
        student.setUser(user);
        user.setStudent(student);

        Hibernate.initialize(student.getUser());
        Hibernate.initialize(student.getAuthorities());
        return student;

    }

}
