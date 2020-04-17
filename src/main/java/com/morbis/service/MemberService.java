package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberComplaint;
import com.morbis.model.member.entity.MemberSearch;
import com.morbis.model.member.repository.MemberComplaintRepository;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.poster.entity.Post;
import com.morbis.model.poster.entity.PosterData;
import com.morbis.model.poster.repository.PostRepository;
import com.morbis.model.poster.repository.PosterDataRepository;
import com.morbis.service.viewable.SearchResult;
import com.morbis.service.viewable.ViewableEntityType;
import jdk.jshell.spi.ExecutionControl;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class MemberService {
    private final GuestService guestService;

    private final PostRepository postRepository;
    private final GameRepository gameRepository;
    private final MemberRepository memberRepository;
    private final PosterDataRepository posterDataRepository;
    private final MemberComplaintRepository memberComplaintRepository;

    public MemberService(GuestService guestService,
                         PostRepository postRepository,
                         GameRepository gameRepository,
                         MemberRepository memberRepository,
                         PosterDataRepository posterDataRepository,
                         MemberComplaintRepository memberComplaintRepository) {
        this.guestService = guestService;
        this.postRepository = postRepository;
        this.gameRepository = gameRepository;
        this.memberRepository = memberRepository;
        this.posterDataRepository = posterDataRepository;
        this.memberComplaintRepository = memberComplaintRepository;
    }

    public void followPage(int memberID, int followableID){
        Optional<PosterData> posterPage = posterDataRepository.findById(followableID);
        if(posterPage.isEmpty())
            throw new IllegalArgumentException("Invalid page id");
        Optional<Member> member = memberRepository.findById(memberID);
        if(member.isEmpty())
            throw new IllegalArgumentException("Invalid member id");

        posterPage.get().getFollowers().add(member.get());
        posterDataRepository.save(posterPage.get());
        member.get().getPagesFollowing().add(posterPage.get());
        memberRepository.save(member.get());
    }

    public void followGame(int memberID, int gameID){
        Optional<Game> game = gameRepository.findById(gameID);
        if(game.isEmpty())
            throw new IllegalArgumentException("Invalid game id");
        Optional<Member> member = memberRepository.findById(memberID);
        if(member.isEmpty())
            throw new IllegalArgumentException("Invalid member id");

        game.get().getFollowers().add(member.get());
        gameRepository.save(game.get());
    }

    public void reportPost(int memberID, int postID, String complaintDescription){
        Optional<Post> post = postRepository.findById(postID);
        if(post.isEmpty())
            throw new IllegalArgumentException("Invalid post id");
        Optional<Member> member = memberRepository.findById(memberID);
        if(member.isEmpty())
            throw new IllegalArgumentException("Invalid member id");
        MemberComplaint complaint = new MemberComplaint(member.get(), post.get(), complaintDescription);
        memberComplaintRepository.save(complaint);
    }

    public List<MemberSearch> getSearchHistory(int memberID) {
        Optional<Member> member = memberRepository.findById(memberID);
        if(member.isEmpty())
            throw new IllegalArgumentException("Invalid member id");
        return member.get().getSearches();
    }

    public Member getMemberInfo(int memberID){
        Optional<Member> member = memberRepository.findById(memberID);
        if(member.isEmpty())
            throw new IllegalArgumentException("Invalid member id");
        return member.get();
    }

    public void updateMemberInfo(Member updatedMember){
        Optional<Member> member = memberRepository.findById(updatedMember.getId());
        if(member.isEmpty())
            throw new IllegalArgumentException("Invalid member id");
        memberRepository.save(updatedMember);
    }

    public Collection<SearchResult> searchData(List<ViewableEntityType> filter, String query, int memberID){
        Optional<Member> member = memberRepository.findById(memberID);
        if(member.isEmpty()) {
            throw new IllegalArgumentException("Invalid member id");
        }
        member.get().getSearches().add(new MemberSearch(query, member.get()));
        memberRepository.save(member.get());
        return guestService.searchData(filter, query);
    }

}
