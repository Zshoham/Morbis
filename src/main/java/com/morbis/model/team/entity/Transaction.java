package com.morbis.model.team.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private int id;

    public Transaction(int id, int value, String description) {
        setId(id);
        setValue(value);
        setDescription(description);
    }

    public Transaction(int value, String description) {
        setValue(value);
        setDescription(description);
    }

    private int value;

    @NotNull
    @NotBlank
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

        if (id != that.id) return false;
        if (value != that.value) return false;
        return description.equals(that.description);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + value;
        result = 31 * result + description.hashCode();
        return result;
    }

    public static TransactionBuilder newPost(int value, String description) {
        return new TransactionBuilder(value, description);
    }

    public static class TransactionBuilder {

        private final Transaction result;

        public TransactionBuilder(int value, String description) {
            result = new Transaction(value, description);
        }

        public TransactionBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public Transaction build() {
            return result;
        }
    }
}
