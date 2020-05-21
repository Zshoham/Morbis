package com.morbis.model.poster.repository;

import com.morbis.model.member.entity.Member;
import com.morbis.model.poster.entity.Post;
import com.morbis.model.poster.entity.PosterData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@DataJpaTest
public class PostRepositoryTest     {

    @Autowired
    private PostRepository postRepository;

    private Post testPost;

    @Before
    public void setUp() {
        testPost = Post.newPost("title", "this is the post content...").build();
        postRepository.save(testPost);
    }

    @Test
    public void save() {
        // saving the same post twice has no effect.
        postRepository.save(testPost);
        assertThat(postRepository.findAll()).containsExactly(testPost);

        // saving a post with the same id and changed content, will cause an update.
        testPost.setTitle("oops..");
        postRepository.save(testPost);
        assertThat(postRepository.findAll()).containsExactly(testPost);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<Post> myPost = postRepository.findById(testPost.getId());
        assertThat(myPost).isPresent();
        assertThat(myPost.get()).isEqualTo(testPost);

        // does not work with invalid id
        Optional<Post> invalidPost = postRepository.findById(testPost.getId() + 1);
        assertThat(invalidPost).isEmpty();
    }
}