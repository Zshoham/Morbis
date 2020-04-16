package com.morbis.model.member.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Member {
    @Id
    @GeneratedValue
    protected int id;

    protected Member(int id, MemberRole role, String username, String password, String name, String email) {
        setId(id);
        setUsername(username);
        setPassword(password);
        setName(name);
        setEmail(email);
        setMemberRole(role);
    }

    protected Member(MemberRole role, String username, String password, String name, String email) {
        setUsername(username);
        setPassword(password);
        setName(name);
        setEmail(email);
        setMemberRole(role);
    }

    @NotNull
    @Enumerated(EnumType.ORDINAL)
    protected MemberRole memberRole;

    @NotNull
    @NotBlank
    @Column(unique = true)
    protected String username;

    @NotNull
    @NotBlank
    protected String password;

    @NotNull
    @NotBlank
    protected String name;

    @NotNull
    @NotBlank
    protected String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;

        Member member = (Member) o;

        if (id != member.id) return false;
        if (memberRole != member.memberRole) return false;
        if (!username.equals(member.username)) return false;
        if (!password.equals(member.password)) return false;
        if (!name.equals(member.name)) return false;
        return email.equals(member.email);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + memberRole.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + password.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + email.hashCode();
        return result;
    }

    public static abstract class MemberBuilder<T extends Member> {

        protected abstract Member getResultMember();

        public void populate(MemberRole role, String username, String password, String name, String email) {
            Member result = getResultMember();
            result.setMemberRole(role);
            result.setUsername(username);
            result.setPassword(password);
            result.setName(name);
            result.setEmail(email);
        }

        public abstract T build();
    }

    public static class AbstractMemberBuilder<BUILDER extends MemberBuilder<?>> {

        private final BUILDER builder;
        private final MemberRole role;

        public AbstractMemberBuilder(MemberRole role, BUILDER builder) {
            this.role = role;
            this.builder = builder;
        }

        public BUILDER fromMember(String username, String password, String name, String email) {
            builder.populate(role, username, password, name, email);

            return builder;
        }

        public BUILDER fromMember(Member member) {
            builder.populate(role, member.getUsername(), member.getPassword(),member.getName(), member.getEmail());

            return builder;
        }
    }
}
