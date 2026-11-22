package com.meditourism.config;

import com.meditourism.role.service.IRoleService;
import com.meditourism.user.entity.UserEntity;
import com.meditourism.user.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private IRoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String adminEmail;
    @Value("${app.admin.password}")
    private String adminPassword;

    @Override
    public void run(String... args) {
        if (adminEmail.isBlank() || adminPassword.isBlank()) {
            log.warn("ADMIN_EMAIL o ADMIN_PASSWORD no están definidos, no se crea el admin");
            return;
        }
        if (userRepository.existsByEmail(adminEmail)) {
            return;
        }
        UserEntity admin = new UserEntity();
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setName("Administrador");
        admin.setRoleEntity(roleService.getRoleById(1L));
        admin.setVerified(true);
        userRepository.save(admin);
        log.info("Admin creado con el correo {}", adminEmail);
    }
}
