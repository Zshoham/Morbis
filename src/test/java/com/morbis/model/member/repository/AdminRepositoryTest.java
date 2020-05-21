package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Admin;
import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    private Admin testAdmin;

    @Before
    public void setUp() {
        testAdmin = Admin.newAdmin()
                .fromMember("admin", "complicated-pass", "master", "cmail")
                .build();

        adminRepository.save(testAdmin);
    }

    @Test
    public void save() {
        // saving the same admin twice has no effect.
        adminRepository.save(testAdmin);
        assertThat(adminRepository.findAll()).containsExactly(testAdmin);

        // saving an admin with the same id and changed content, will cause an update.
        testAdmin.setUsername("cool-admin");
        adminRepository.save(testAdmin);
        assertThat(adminRepository.findAll()).containsExactly(testAdmin);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Admin> admin = adminRepository.findById(testAdmin.getId());
        assertThat(admin).isPresent();
        assertThat(admin.get()).isEqualTo(testAdmin);

        // does not work with invalid id
        Optional<Admin> invalidAdmin = adminRepository.findById(testAdmin.getId() + 1);
        assertThat(invalidAdmin).isEmpty();
    }
}