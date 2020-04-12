package com.morbis.model.member.entity;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Fan extends Member {

    public Fan(int id, String username, String password, String name, String email) {
        super(id, MemberRole.FAN, username, password, name, email);
    }

    public Fan(String username, String password, String name, String email) {
        super(MemberRole.FAN, username, password, name, email);
    }

    public static AbstractMemberBuilder<FanBuilder> newFan() {
        return new AbstractMemberBuilder<>(MemberRole.FAN, new FanBuilder());
    }

    public static class FanBuilder extends MemberBuilder<Fan> {

        private final Fan result;

        public FanBuilder() {
            result = new Fan();
        }

        @Override
        protected Member getResultMember() {
            return result;
        }

        public FanBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        @Override
        public Fan build() {
            return result;
        }
    }
}
