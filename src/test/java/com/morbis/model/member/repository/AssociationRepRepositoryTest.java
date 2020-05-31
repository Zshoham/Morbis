package com.morbis.model.member.repository;

import com.morbis.model.member.entity.AssociationRep;
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
public class AssociationRepRepositoryTest {

    @Autowired
    private AssociationRepRepository associationRepRepository;

    private AssociationRep testAssociationRep;

    @Before
    public void setUp() {
        testAssociationRep = AssociationRep.newAssociationRep()
                .fromMember("user", "pass", "name", "email")
                .build();

        associationRepRepository.save(testAssociationRep);
    }

    @Test
    public void save() {
        // saving the same association rep twice has no effect.
        associationRepRepository.save(testAssociationRep);
        assertThat(associationRepRepository.findAll()).containsExactly(testAssociationRep);

        // saving an association rep with the same id and changed content, will cause an update.
        testAssociationRep.setUsername("secondUser");
        associationRepRepository.save(testAssociationRep);
        assertThat(associationRepRepository.findAll()).containsExactly(testAssociationRep);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<AssociationRep> associationRep = associationRepRepository.findById(this.testAssociationRep.getId());
        assertThat(associationRep).isPresent();
        assertThat(associationRep.get()).isEqualTo(this.testAssociationRep);

        // does not work with invalid id
        Optional<AssociationRep> invalidassociationRep = associationRepRepository.findById(this.testAssociationRep.getId() + 1);
        assertThat(invalidassociationRep).isEmpty();
    }
}