package com.morbis.model.member.entity;

import com.morbis.model.poster.entity.Post;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberComplaint {
    @Id
    @GeneratedValue
    private int id;

    public MemberComplaint(int id, Member member, Post post) {
        setId(id);
        setMember(member);
        setPost(post);
    }

    public MemberComplaint(Member member, Post post) {
        setMember(member);
        setPost(post);
    }

    @NotNull
    @ManyToOne(targetEntity = Member.class)
    private Member member;

    @NotNull
    @ManyToOne(targetEntity = Post.class)
    private Post post;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MemberComplaint)) return false;

        MemberComplaint that = (MemberComplaint) o;

        if (id != that.id) return false;
        if (!member.equals(that.member)) return false;
        return post.equals(that.post);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + member.hashCode();
        result = 31 * result + post.hashCode();
        return result;
    }

    public static ComplaintBuilder newPost(Member member, Post post) {
        return new ComplaintBuilder(member, post);
    }

    public static class ComplaintBuilder {

        private final MemberComplaint result;

        public ComplaintBuilder(Member member, Post post) {
            result = new MemberComplaint(member, post);
        }

        public ComplaintBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public MemberComplaint build() {
            return result;
        }
    }
}
