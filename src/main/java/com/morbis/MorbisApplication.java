package com.morbis;

import com.morbis.model.member.entity.Admin;
import com.morbis.model.member.repository.AdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MorbisApplication implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(MorbisApplication.class);

    private final AdminRepository adminRepository;

    public MorbisApplication(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MorbisApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!args.getOptionNames().contains("admin"))
            return;

        logger.info("Creating new admin from command line arguments");

        String username = args.getOptionValues("username").get(0);
        String password = args.getOptionValues("password").get(0);
        String name = args.getOptionValues("name").get(0);
        String email = args.getOptionValues("email").get(0);

        Admin admin = Admin.newAdmin()
                .fromMember(username, password, name, email)
                .build();

        adminRepository.save(admin);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
