package com.morbis.model.member.repository;

import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.TeamOwnerRegRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static com.morbis.data.MemberServiceDataSource.*;
import static org.assertj.core.api.Assertions.catchThrowable;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TeamOwnerRegRequestRepositoryTest {

    @Autowired
    private TeamOwnerRegRequestRepository teamOwnerRegRequestRepository;
    @Autowired
    private MemberRepository memberRepository;

    private TeamOwnerRegRequest testRequest;

    @Before
    public void setUp(){
        initWithoutID();
        memberRepository.save(simpleMember);
        testRequest = new TeamOwnerRegRequest(simpleMember, "new team");
        teamOwnerRegRequestRepository.save(testRequest);
    }

    @Test
    public void findById(){
        // works with correct id
        Optional<TeamOwnerRegRequest> request = teamOwnerRegRequestRepository.findById(simpleMember.getId());
        assertThat(request).isPresent();
        assertThat(request.get()).isEqualTo(testRequest);
        assertThat(request.get().getRequestingMember()).isEqualTo(simpleMember);
        Optional<Member> requesting = memberRepository.findById(request.get().getId());
        assertThat(requesting).isPresent();
        assertThat(requesting.get()).isEqualTo(simpleMember);

        // does not work with invalid id
        Optional<TeamOwnerRegRequest> invalidReq = teamOwnerRegRequestRepository.findById(999);
        assertThat(invalidReq).isEmpty();
    }

    @Test
    public void deleteById(){
        // delete if id valid
        Optional<TeamOwnerRegRequest> request = teamOwnerRegRequestRepository.findById(simpleMember.getId());
        assertThat(request).isPresent();
        teamOwnerRegRequestRepository.deleteById(simpleMember.getId());
        request = teamOwnerRegRequestRepository.findById(simpleMember.getId());
        assertThat(request).isEmpty();

        //do nothing for invalid id
        Throwable expectedException = catchThrowable(() -> teamOwnerRegRequestRepository.deleteById(999));
        assertThat(expectedException)
                .hasMessageContainingAll("No", "entity", "id", "999");
    }

}