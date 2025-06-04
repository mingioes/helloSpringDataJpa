package kr.ac.hansung.cse.hellospringdatajpa.config;

import kr.ac.hansung.cse.hellospringdatajpa.entity.Role;
import kr.ac.hansung.cse.hellospringdatajpa.entity.User;
import kr.ac.hansung.cse.hellospringdatajpa.repo.RoleRepository;
import kr.ac.hansung.cse.hellospringdatajpa.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 기본 역할(Role) 생성
        createRolesIfNotExists();
        
        // 기본 관리자 계정 생성
        createAdminUserIfNotExists();
        
        // 기본 일반 사용자 계정 생성
        createUserIfNotExists();
    }

    private void createRolesIfNotExists() {
        // ROLE_USER 생성
        if (!roleRepository.existsByName("ROLE_USER")) {
            Role userRole = new Role("ROLE_USER", "일반 사용자");
            roleRepository.save(userRole);
            System.out.println("ROLE_USER created");
        }

        // ROLE_ADMIN 생성
        if (!roleRepository.existsByName("ROLE_ADMIN")) {
            Role adminRole = new Role("ROLE_ADMIN", "관리자");
            roleRepository.save(adminRole);
            System.out.println("ROLE_ADMIN created");
        }
    }

    private void createAdminUserIfNotExists() {
        String adminEmail = "admin@hansung.ac.kr";
        
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setName("관리자");
            admin.setEnabled(true);

            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(roleRepository.findByName("ROLE_USER").get());
            adminRoles.add(roleRepository.findByName("ROLE_ADMIN").get());
            admin.setRoles(adminRoles);

            userRepository.save(admin);
            System.out.println("Admin user created: " + adminEmail + " / password: admin123");
        }
    }

    private void createUserIfNotExists() {
        String userEmail = "user@hansung.ac.kr";
        
        if (!userRepository.existsByEmail(userEmail)) {
            User user = new User();
            user.setEmail(userEmail);
            user.setPassword(passwordEncoder.encode("user123"));
            user.setName("일반사용자");
            user.setEnabled(true);

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(roleRepository.findByName("ROLE_USER").get());
            user.setRoles(userRoles);

            userRepository.save(user);
            System.out.println("Regular user created: " + userEmail + " / password: user123");
        }
    }
}