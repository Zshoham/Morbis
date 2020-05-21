package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Fan;
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
public class FanRepositoryTest {

    @Autowired
    private FanRepository fanRepository;

    private Fan testFan;

    @Before
    public void setUp() {
        testFan = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .build();

        fanRepository.save(testFan);
    }

    @Test
    public void save() {
        // saving the same fan twice has no effect.
        fanRepository.save(testFan);
        assertThat(fanRepository.findAll()).containsExactly(testFan);

        // saving a fan with the same id and changed content, will cause an update.
        testFan.setUsername("secondUser");
        fanRepository.save(testFan);
        assertThat(fanRepository.findAll()).containsExactly(testFan);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Fan> fan = fanRepository.findById(this.testFan.getId());
        assertThat(fan).isPresent();
        assertThat(fan.get()).isEqualTo(this.testFan);

        // does not work with invalid id
        Optional<Fan> invalidFan = fanRepository.findById(this.testFan.getId() + 1);
        assertThat(invalidFan).isEmpty();
    }
}