package com.morbis.model.poster.entity;

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
public class Post {
    @Id
    @GeneratedValue
    private int id;

    public Post(int id, String title, String content) {
        setId(id);
        setTitle(title);
        setContent(content);
    }

    public Post(String title, String content) {
        setTitle(title);
        setContent(content);
    }

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        if (id != post.id) return false;
        if (!title.equals(post.title)) return false;
        return content.equals(post.content);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }

    public static PostBuilder newPost(String title, String content) {
        return new PostBuilder(title, content);
    }

    public static class PostBuilder {

        private final Post result;

        public PostBuilder(String title, String content) {
            result = new Post(title, content);
        }

        public PostBuilder withId(int id) {
            result.setId(id);
            return this;
        }

        public Post build() {
            return result;
        }
    }
}
