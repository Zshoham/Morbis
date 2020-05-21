package com.morbis.model.team.repository;

import com.morbis.model.team.entity.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    private Transaction testTransaction;

    @Before
    public void setUp() {
        testTransaction = Transaction.newTransaction(10, "this is good transaction").build();
        transactionRepository.save(testTransaction);
    }

    @Test
    public void save() {
        // saving the same transaction twice has no effect.
        transactionRepository.save(testTransaction);
        assertThat(transactionRepository.findAll()).containsExactly(testTransaction);

        // saving a transaction with the same id and changed content, will cause an update.
        testTransaction.setValue(1000);
        transactionRepository.save(testTransaction);
        assertThat(transactionRepository.findAll()).containsExactly(testTransaction);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Transaction> myTransaction = transactionRepository.findById(testTransaction.getId());
        assertThat(myTransaction).isPresent();
        assertThat(myTransaction.get()).isEqualTo(testTransaction);

        // does not work with invalid id
        Optional<Transaction> invalidTransaction = transactionRepository.findById(testTransaction.getId() + 1);
        assertThat(invalidTransaction).isEmpty();
    }
}