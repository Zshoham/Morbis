package com.morbis.model.member.entity;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Admin extends Member {

    public Admin(int id, String username, String password, String name, String email) {
        super(id, MemberRole.ADMIN, username, password, name, email);
    }

    public Admin(String username, String password, String name, String email) {
        super(MemberRole.ADMIN, username, password, name, email);
    }

    public static AbstractMemberBuilder<AdminBuilder> newAdmin() {
        return new AbstractMemberBuilder<>(MemberRole.ADMIN, new AdminBuilder());
    }

    public static class AdminBuilder extends MemberBuilder<Admin> {

        private final Admin result;

        public AdminBuilder() {
            result = new Admin();
        }

        @Override
        protected Member getResultMember() {
            return result;
        }

        public AdminBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        @Override
        public Admin build() {
            return result;
        }
    }
}
