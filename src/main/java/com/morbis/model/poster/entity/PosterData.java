package com.morbis.model.poster.entity;

import com.morbis.model.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PosterData {
    @Id
    @GeneratedValue
    private int id;

    @ManyToMany(targetEntity = Member.class)
    private List<Member> followers;

    @OneToMany(targetEntity = Post.class)
    private List<Post> posts;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PosterData)) return false;

        PosterData that = (PosterData) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return 31 + 31 * id;
    }

    public PosterDataBuilder newPosterData() {
        return new PosterDataBuilder();
    }

    public static class PosterDataBuilder {

        private final PosterData result;

        public PosterDataBuilder() {
            this.result = new PosterData();
        }

        public PosterDataBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public PosterDataBuilder withFollowers(List<Member> followers) {
            result.setFollowers(followers);
            return this;
        }

        public PosterDataBuilder withPosts(List<Post> posts) {
            result.setPosts(posts);
            return this;
        }

        public PosterData build() {
            return result;
        }
    }
}
