package com.morbis.model.poster;

import com.morbis.model.member.Member;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PosterData {
    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @OneToMany(targetEntity = Member.class)
    private List<Member> followers;

    @NotNull
    @OneToMany(targetEntity = Post.class)
    private List<Post> posts;
}
