package com.morbis.model.member.entity;

import com.morbis.model.poster.entity.Post;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MemberComplaint {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @NotBlank
    private String complaintDescription;

    public MemberComplaint(int id, Member member, Post post, String complaintDescription) {
        setId(id);
        setMember(member);
        setPost(post);
        setComplaintDescription(complaintDescription);
    }

    public MemberComplaint(Member member, Post post, String complaintDescription) {
        setMember(member);
        setPost(post);
        setComplaintDescription(complaintDescription);
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

    public static ComplaintBuilder newComplaint(Member member, Post post, String complaintDescription) {
        return new ComplaintBuilder(member, post, complaintDescription);
    }

    public static class ComplaintBuilder {

        private final MemberComplaint result;

        public ComplaintBuilder(Member member, Post post, String complaintDescription) {
            result = new MemberComplaint(member, post, complaintDescription);
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
