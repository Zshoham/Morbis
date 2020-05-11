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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private Logger logger;

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
        this.logger = LoggerFactory.getLogger(MemberService.class);

    }

    public void followPage(int memberID, int followableID) {
        logger.trace("called function: MemberService->followPage.");
        Optional<PosterData> posterPage = posterDataRepository.findById(followableID);
        if (posterPage.isEmpty()) {
            logger.error("page with the followableID: " + followableID + " not found");
            throw new IllegalArgumentException("Invalid page id");
        }

        Optional<Member> member = memberRepository.findById(memberID);
        if (member.isEmpty()) {
            logger.error("member with the ID: " + memberID + " not found");
            throw new IllegalArgumentException("Invalid member id");
        }

        posterPage.get().getFollowers().add(member.get());
        posterDataRepository.save(posterPage.get());
        member.get().getPagesFollowing().add(posterPage.get());
        memberRepository.save(member.get());
        logger.info(memberID + " followed the page with the ID: " + followableID);
    }

    public void followGame(int memberID, int gameID) {
        logger.trace("called function: MemberService->followGame.");
        Optional<Game> game = gameRepository.findById(gameID);
        if (game.isEmpty()) {
            logger.error("no such a game with the ID: " + gameID);
            throw new IllegalArgumentException("Invalid game id");
        }
        Optional<Member> member = memberRepository.findById(memberID);
        if (member.isEmpty()) {
            logger.error("no such a member with the ID: " + memberID);
            throw new IllegalArgumentException("Invalid member id");
        }

        game.get().getFollowers().add(member.get());
        gameRepository.save(game.get());
        logger.info(memberID + " followed the game with the ID: " + gameID);

    }

    public void reportPost(int memberID, int postID, String complaintDescription) {
        logger.trace("called function: MemberService->reportPost.");

        Optional<Post> post = postRepository.findById(postID);
        if (post.isEmpty()) {
            logger.error("no such a post with the ID: " + postID);
            throw new IllegalArgumentException("Invalid post id");
        }
        Optional<Member> member = memberRepository.findById(memberID);
        if (member.isEmpty()) {
            logger.error("no such a member with the ID: " + memberID);
            throw new IllegalArgumentException("Invalid member id");
        }
        MemberComplaint complaint = new MemberComplaint(member.get(), post.get(), complaintDescription);
        memberComplaintRepository.save(complaint);
        logger.info(memberID + " reported a post with the ID: " + postID);

    }

    public List<MemberSearch> getSearchHistory(int memberID) {
        logger.trace("called function: MemberService->getSearchHistory.");

        Optional<Member> member = memberRepository.findById(memberID);
        if (member.isEmpty()) {
            logger.error("no such a member with the ID: " + memberID);
            throw new IllegalArgumentException("Invalid member id");
        }
        logger.info("search history of the member " + memberID + " has been returned.");
        return member.get().getSearches();
    }

    public Member getMemberInfo(int memberID) {
        logger.trace("called function: MemberService->getMemberInfo.");
        Optional<Member> member = memberRepository.findById(memberID);
        if (member.isEmpty()) {
            logger.error("no such a member with the ID: " + memberID);
            throw new IllegalArgumentException("Invalid member id");
        }
        logger.info("info about the member " + memberID + " has been returned.");
        return member.get();
    }

    public void updateMemberInfo(Member updatedMember) {
        logger.trace("called function: MemberService->updateMemberInfo.");
        Optional<Member> member = memberRepository.findById(updatedMember.getId());
        if (member.isEmpty()) {
            logger.error("no such a member with the ID: " + updatedMember.getId());
            throw new IllegalArgumentException("Invalid member id");
        }
        memberRepository.save(updatedMember);
        logger.info("the info of member: " + updatedMember.getId() + " has been updated.");
    }

    public Collection<SearchResult> searchData(List<ViewableEntityType> filter, String query, int memberID) {
        logger.trace("called function: MemberService->searchData.");
        Optional<Member> member = memberRepository.findById(memberID);
        if (member.isEmpty()) {
            logger.error("no such a member with the ID: " + memberID);
            throw new IllegalArgumentException("Invalid member id");
        }
        member.get().getSearches().add(new MemberSearch(query, member.get()));
        memberRepository.save(member.get());
        logger.info("search data of the member: " + memberID + " has returned");
        return guestService.searchData(filter, query);
    }

}
