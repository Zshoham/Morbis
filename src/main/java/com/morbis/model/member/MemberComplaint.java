package com.morbis.model.member;

import com.morbis.model.poster.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MemberComplaint {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @ManyToOne(targetEntity = Member.class)
    private Member member;

    @NotNull
    @ManyToOne(targetEntity = Post.class)
    private Post post;
}
