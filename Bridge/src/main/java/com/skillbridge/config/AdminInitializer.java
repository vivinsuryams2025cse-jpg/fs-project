package com.skillbridge.config;

import com.skillbridge.entity.Student;
import com.skillbridge.enums.UserRole;
import com.skillbridge.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AdminInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;

    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.admin.full-name}")
    private String adminFullName;

    public AdminInitializer(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (!studentRepository.existsByEmailIgnoreCase(adminEmail)) {
            Student admin = new Student();
            admin.setFullName(adminFullName);
            admin.setEmail(adminEmail.toLowerCase().trim());
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setCollegeName("SkillBridge Platform");
            admin.setDepartment("Administration");
            admin.setYearOfStudy(4);
            admin.setBio("System Administrator Account");
            admin.setRole(UserRole.ADMIN);
            admin.setEnabled(true);

            studentRepository.save(admin);
            log.info("Initialized default ADMIN account: {}", adminEmail);
        }
    }
}
