package com.morbis.model.member.entity;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AssociationRep extends Member {

    public AssociationRep(int id, String username, String password, String name, String email) {
        super(id, MemberRole.ASSOCIATION_REP, username, password, name, email);
    }

    public AssociationRep(String username, String password, String name, String email) {
        super(MemberRole.ASSOCIATION_REP, username, password, name, email);
    }

    public static AbstractMemberBuilder<AssociationRepBuilder> newAssociationRep() {
        return new AbstractMemberBuilder<>(MemberRole.ASSOCIATION_REP, new AssociationRepBuilder());
    }

    public static class AssociationRepBuilder extends MemberBuilder<AssociationRep> {

        private final AssociationRep result;

        public AssociationRepBuilder() {
            result = new AssociationRep();
        }

        @Override
        protected Member getResultMember() {
            return result;
        }

        public AssociationRep.AssociationRepBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        @Override
        public AssociationRep build() {
            return result;
        }
    }
}
