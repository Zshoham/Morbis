package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Fan;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberComplaint;
import com.morbis.model.poster.entity.Post;
import com.morbis.model.poster.repository.PostRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class MemberComplaintRepositoryTest {

    @Autowired
    private MemberComplaintRepository memberComplaintRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;


    private MemberComplaint testMemberComplaint;

    @Before
    public void setUp() {
        Member testMember = Fan.newFan()
                .fromMember("user", "pass", "name", "email")
                .build();
        memberRepository.save(testMember);

        Post testPost = Post.newPost("title", "content").build();
        postRepository.save(testPost);

        testMemberComplaint = MemberComplaint.newComplaint(testMember, testPost, "I'm angry about it!")
                .build();

        memberComplaintRepository.save(testMemberComplaint);
    }

    @Test
    public void save() {
        // saving the same complaint twice has no effect.
        memberComplaintRepository.save(testMemberComplaint);
        assertThat(memberComplaintRepository.findAll()).containsExactly(testMemberComplaint);

        // saving a complaint with the same id and changed content, will cause an update.
        testMemberComplaint.setComplaintDescription("I'm very angry about it!!!");
        memberComplaintRepository.save(testMemberComplaint);
        assertThat(memberComplaintRepository.findAll()).containsExactly(testMemberComplaint);
    }

    @Test
    public void findById() {
        // works with correct id
        Optional<MemberComplaint> memberComplaint = memberComplaintRepository.findById(this.testMemberComplaint.getId());
        assertThat(memberComplaint).isPresent();
        assertThat(memberComplaint.get()).isEqualTo(this.testMemberComplaint);

        // does not work with invalid id
        Optional<MemberComplaint> invalidMemberComplaint = memberComplaintRepository.findById(this.testMemberComplaint.getId() + 1);
        assertThat(invalidMemberComplaint).isEmpty();
    }
}