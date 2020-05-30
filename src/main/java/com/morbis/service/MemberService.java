package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.game.entity.GameEvent;
import com.morbis.model.game.repository.GameRepository;
import com.morbis.model.member.entity.*;
import com.morbis.model.member.repository.*;
import com.morbis.model.poster.entity.Post;
import com.morbis.model.poster.entity.PosterData;
import com.morbis.model.poster.repository.PostRepository;
import com.morbis.model.poster.repository.PosterDataRepository;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.service.viewable.SearchResult;
import com.morbis.service.viewable.ViewableEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
public class MemberService {
    private final GuestService guestService;

    private final TeamRepository teamRepository;
    private final PostRepository postRepository;
    private final GameRepository gameRepository;
    private final CoachRepository coachRepository;
    private final MemberRepository memberRepository;
    private final PlayerRepository playerRepository;
    private final TeamOwnerRepository teamOwnerRepository;
    private final PosterDataRepository posterDataRepository;
    private final MemberComplaintRepository memberComplaintRepository;
    private final TeamOwnerRegRequestRepository teamOwnerRegRequestRepository;
    private final Logger logger;

    public MemberService(GuestService guestService,
                         TeamRepository teamRepository,
                         PostRepository postRepository,
                         GameRepository gameRepository,
                         CoachRepository coachRepository,
                         PlayerRepository playerRepository,
                         MemberRepository memberRepository,
                         TeamOwnerRepository teamOwnerRepository,
                         PosterDataRepository posterDataRepository,
                         MemberComplaintRepository memberComplaintRepository,
                         TeamOwnerRegRequestRepository teamOwnerRegRequestRepository) {

        this.guestService = guestService;
        this.teamRepository = teamRepository;
        this.postRepository = postRepository;
        this.gameRepository = gameRepository;
        this.memberRepository = memberRepository;
        this.posterDataRepository = posterDataRepository;
        this.memberComplaintRepository = memberComplaintRepository;
        this.coachRepository = coachRepository;
        this.playerRepository = playerRepository;
        this.teamOwnerRepository = teamOwnerRepository;
        this.teamOwnerRegRequestRepository = teamOwnerRegRequestRepository;
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
        member.get().getGamesFollowing().add(game.get());
        memberRepository.save(member.get());
        logger.info(memberID + " followed the game with the ID: " + gameID);
    }

    public List<Game> getGamesFollowing(int memberID) {
        logger.trace("called function: MemberService->getGamesFollowing.");
        Member member = memberRepository.findById(memberID)
                .orElseThrow(() -> new IllegalArgumentException("member with id " + memberID + " does not exist"));

        return Objects.requireNonNullElse(member.getGamesFollowing(), new LinkedList<>());
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

    public void registerAsCoach(int memberID, String qualification, String role) {
        logger.trace("called function: MemberService->registerAsCoach.");
        Optional<Member> member = memberRepository.findById(memberID);
        if (member.isEmpty()) {
            logger.error("no such a member with the ID: " + memberID);
            throw new IllegalArgumentException("Invalid member id");
        }

        if (member.get().getMemberRole().contains(MemberRole.COACH)) {
            throw new IllegalArgumentException("The member is already a coach");
        }

        member.get().getMemberRole().add(MemberRole.COACH);
        Coach coach = Coach.newCoach(qualification, role).fromMember(member.get()).build();

        memberRepository.save(member.get());
        coachRepository.save(coach);
        logger.info(memberID + "[memberID] was registered as a coach");
    }

    public void registerAsPlayer(int memberID, LocalDate birthday, String position) {
        logger.trace("called function: MemberService->registerAsPlayer.");
        Optional<Member> member = memberRepository.findById(memberID);
        if (member.isEmpty()) {
            logger.error("no such a member with the ID: " + memberID);
            throw new IllegalArgumentException("Invalid member id");
        }

        if (member.get().getMemberRole().contains(MemberRole.PLAYER)) {
            throw new IllegalArgumentException("The member is already a player");
        }

        member.get().getMemberRole().add(MemberRole.PLAYER);
        Player player = Player.newPlayer(birthday, position).fromMember(member.get()).build();

        memberRepository.save(member.get());
        playerRepository.save(player);
        logger.info(memberID + "[memberID] was registered as a player");
    }

    protected Member validateRegisterAsTeamOwner(int memberID, String teamName){
        Optional<Member> member = memberRepository.findById(memberID);
        if (member.isEmpty()) {
            logger.error("no such a member with the ID: " + memberID);
            throw new IllegalArgumentException("Invalid member id");
        }

        if (member.get().getMemberRole().contains(MemberRole.TEAM_OWNER))
            throw new IllegalArgumentException("The member is already a team owner");

        boolean teamExists = teamRepository.findAllByNameContaining(teamName)
                .stream().map(Team::getName).anyMatch(name-> name.equals(teamName));
        if(teamExists)
            throw new IllegalArgumentException("team already exist");
        return member.get();
    }

    public void requestRegisterAsTeamOwner(int memberID, String teamName) {
        logger.trace("called function: MemberService->requestRegisterAsTeamOwner.");
        Member requestingMember = validateRegisterAsTeamOwner(memberID, teamName);
        boolean memberRequested = teamOwnerRegRequestRepository.findById(memberID).isPresent();
        if(memberRequested)
            throw new IllegalArgumentException("member already requested to assign as team owner- request pending");
        TeamOwnerRegRequest request = new TeamOwnerRegRequest(requestingMember, teamName);
        teamOwnerRegRequestRepository.save(request);
        logger.info(memberID + "[memberID] requested to register as a team owner");
    }

    public void registerAsTeamOwner(int memberID, String teamName){
        logger.trace("called function: MemberService->requestRegisterAsTeamOwner.");
        Member member = validateRegisterAsTeamOwner(memberID, teamName);
        member.getMemberRole().add(MemberRole.TEAM_OWNER);
        TeamOwner owner = TeamOwner.newTeamOwner().fromMember(member).build();
        memberRepository.save(member);
        teamOwnerRepository.save(owner);
        Team newTeam = Team.newTeam()
                .name(teamName)
                .players(new LinkedList<>())
                .owners(List.of(owner))
                .coaches(new LinkedList<>())
                .stadium(null)
                .build();

        teamRepository.save(newTeam);
        owner.setTeam(newTeam);
        teamOwnerRepository.save(owner);
        logger.info(memberID + "[memberID] requested to register as a team owner");
    }

    public void removeRoleFromMember(int memberID, MemberRole role) {
        logger.trace("called function: MemberService->removeRoleFromMember.");
        Member member = memberRepository.findById(memberID)
                .orElseThrow(() -> new IllegalArgumentException("Invalid member id"));

        if (role == null)
            throw new IllegalArgumentException("Invalid member role");

        member.getMemberRole().remove(role);
        memberRepository.save(member);

        logger.info(memberID + "[memberID] is no longer a " + role.toString());
    }

    public List<GameEvent> getEventsBackLog(int memberID) {
        Member member = memberRepository.findById(memberID)
                .orElseThrow(() -> new IllegalArgumentException("member with id " + memberID + " not found"));

        List<GameEvent> events = new ArrayList<>(member.getEventBackLog());

        member.setEventBackLog(new LinkedList<>());
        memberRepository.save(member);

        return events;
    }

    public int getEventBacklogSize(int memberID) {
        Member member = memberRepository.findById(memberID)
                .orElseThrow(() -> new IllegalArgumentException("member with id " + memberID + " not found"));

        return member.getEventBackLog().size();
    }
}
