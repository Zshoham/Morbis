package com.morbis;

import com.morbis.model.member.entity.Admin;
import com.morbis.model.member.entity.AssociationRep;
import com.morbis.model.member.repository.AdminRepository;
import com.morbis.model.member.repository.AssociationRepRepository;
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

    private final AssociationRepRepository associationRepRepository;

    public MorbisApplication(AdminRepository adminRepository, AssociationRepRepository associationRepRepository) {
        this.adminRepository = adminRepository;
        this.associationRepRepository = associationRepRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MorbisApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!args.getOptionNames().contains("setup"))
            return;

        logger.info("Creating new admin from command line arguments");

        String admin_username = args.getOptionValues("Ausername").get(0);
        String admin_password = args.getOptionValues("Apassword").get(0);
        String admin_name = args.getOptionValues("Aname").get(0);
        String admin_email = args.getOptionValues("Aemail").get(0);

        String rep_username = args.getOptionValues("Rusername").get(0);
        String rep_password = args.getOptionValues("Rpassword").get(0);
        String rep_name = args.getOptionValues("Rname").get(0);
        String rep_email = args.getOptionValues("Remail").get(0);

        Admin admin = Admin.newAdmin()
                .fromMember(admin_username, admin_password, admin_name, admin_email)
                .build();

        AssociationRep rep = AssociationRep.newAssociationRep()
                .fromMember(rep_username, rep_password, rep_name, rep_email)
                .build();

        adminRepository.save(admin);
        associationRepRepository.save(rep);
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
