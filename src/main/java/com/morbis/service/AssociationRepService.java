package com.morbis.service;

import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.SchedulingMethod;
import com.morbis.model.league.entity.ScoringMethod;
import com.morbis.model.league.entity.Season;
import com.morbis.model.league.repository.LeagueRepository;
import com.morbis.model.league.repository.SeasonRepository;
import com.morbis.model.member.entity.Referee;
import com.morbis.model.member.entity.TeamOwnerRegRequest;
import com.morbis.model.member.repository.RefereeRepository;
import com.morbis.model.member.repository.TeamOwnerRegRequestRepository;
import com.morbis.service.notification.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class AssociationRepService {

    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;
    private final RefereeRepository refereeRepository;
    private final TeamOwnerRegRequestRepository teamOwnerRegRequestRepository;

    private final EmailService emailService;
    private final MemberService memberService;
    private final Logger logger;

    public AssociationRepService(EmailService emailService,
                                 MemberService memberService,
                                 SeasonRepository seasonRepository,
                                 LeagueRepository leagueRepository,
                                 RefereeRepository refereeRepository,
                                 TeamOwnerRegRequestRepository teamOwnerRegRequestRepository) {
        this.emailService = emailService;
        this.memberService = memberService;
        this.seasonRepository = seasonRepository;
        this.leagueRepository = leagueRepository;
        this.refereeRepository = refereeRepository;
        this.teamOwnerRegRequestRepository = teamOwnerRegRequestRepository;
        this.logger = LoggerFactory.getLogger(AssociationRepService.class);
    }


    public List<League> getLeagues() {
        logger.trace("called function: AssociationRepService->getLeagues.");
        return leagueRepository.findAll();
    }

    public boolean addLeague(String leagueName) {
        logger.trace("Called function: AssociationRepService->addLeague. with the name of: " + leagueName + ".");
        if (leagueRepository.findAllByName(leagueName).isEmpty()) {
            leagueRepository.save(new League(leagueName));
            logger.info("League " + leagueName + " has been created.");
            return true;
        }
        logger.warn("League " + leagueName + " is already exist.");
        return false;
    }

    public void addSeason(int leagueID, int year) throws IllegalArgumentException {
        logger.trace("called function: AssociationRepService->addSeason. with the leagueID: " + leagueID + " and the year: " + year + ".");
        League league = leagueRepository.findById(leagueID)
                .orElseThrow(() -> new IllegalArgumentException("trying to add season to non existent league"));

        if (seasonRepository.findByLeagueAndYear(league, year).isPresent()) {
            logger.error("the league " + league.getName() + "already contains the " + year + "season.");
            throw new IllegalArgumentException("the league " + league.getName() + "already contains the " + year + "season.");
        }

        Season newSeason = new Season(year, league);
        seasonRepository.save(newSeason);
        league.getSeasons().add(newSeason);
        leagueRepository.save(league);
        logger.info("season added to league: " + leagueID + " at the year: " + year + ".");
    }

    public List<Season> getSeasons(int leagueID) {
        logger.trace("called function: AssociationRepService->getSeason.");
        List<Season> result = new ArrayList<>();
        leagueRepository.findById(leagueID).ifPresent(league ->
                result.addAll(league.getSeasons()));
        logger.info("The seasons of league :" + leagueID + " has been returned.");
        return result;
    }

    public List<ScoringMethod> getScoringMethods() {
        logger.trace("called function: AssociationRepService->getScoringMethods.");
        return Stream.of(ScoringMethod.values()).collect(Collectors.toList());
    }

    public List<SchedulingMethod> getSchedulingMethods() {
        logger.trace("called function: AssociationRepService->getSchedulingMethods");
        return Stream.of(SchedulingMethod.values()).collect(Collectors.toList());
    }

    public void setScoringMethod(int leagueID, ScoringMethod method) throws IllegalArgumentException {
        logger.trace("called function: AssociationRepService->setScoringMethod. leagueID: " + leagueID);
        League league = leagueRepository.findById(leagueID)
                .orElseThrow(() -> new IllegalArgumentException("trying to change scoring method on non existent league"));
        league.setScoringMethod(method);
        leagueRepository.save(league);
        logger.info("Scoring method has been set to the league with the ID of: " + leagueID);
    }

    public void setSchedulingMethod(int leagueID, SchedulingMethod method) throws IllegalArgumentException {
        logger.trace("called function: AssociationRepService->setSchedulingMethod. to the league with the ID of: " + leagueID);
        League league = leagueRepository.findById(leagueID)
                .orElseThrow(() -> new IllegalArgumentException("trying to change scheduling method on non existent league"));

        league.setSchedulingMethod(method);
        leagueRepository.save(league);
    }

    public void addRef(String email, String name) throws MessagingException {
        logger.trace("called function: AssociationRepService->addRef. with the email: " + email + " and the name of: " + name);
        String tempUsername = UUID.randomUUID().toString();
        String tempPassword = UUID.randomUUID().toString();

        Referee newAccount = Referee.newReferee("NA")
                .fromMember(tempUsername, tempPassword, name, email)
                .build();

        emailService.registerReferee(newAccount);
        refereeRepository.save(newAccount);
        logger.info("Referee added. name: " + name + ", and the email: " + email);
    }

    public List<Referee> getRefs() {
        logger.trace("called function: AssociationRepService->getRefs.");
        return refereeRepository.findAll();
    }


    public void addRefsToSeason(int chosenSeasonID, List<Integer> refIDs) {
        logger.trace("called function: AssociationRepService->addRefsToSeason.");
        Season chosen = seasonRepository.findById(chosenSeasonID).orElseThrow(
                () -> new IllegalArgumentException("the chosen season does not exist.")
        );
        List<Referee> referees = refereeRepository.findAllById(refIDs);
        chosen.getReferees().addAll(referees);
        seasonRepository.save(chosen);

        logger.info("Referees added to the season: " + chosenSeasonID);
    }

    public void removeRefs(List<Integer> refIDs) {
        logger.trace("called function: AssociationRepService->removeRefs.");
        List<Referee> referees = refereeRepository.findAllById(refIDs);

        for (Referee ref : referees) {
            for (Season season : ref.getSeasons()) {
                season.getReferees().remove(ref);
                seasonRepository.save(season);
            }
        }

        referees.forEach(refereeRepository::delete);
        logger.info("Referees has been removed.");
    }

    public List<TeamOwnerRegRequest> getAllPendingRequests() {
        return teamOwnerRegRequestRepository.findAll();
    }

    public void handleNewTeamOwnerRequest(int requestingMemberId, boolean approved) {
        TeamOwnerRegRequest request = teamOwnerRegRequestRepository.findById(requestingMemberId)
                .orElseThrow(() -> new IllegalArgumentException("request not found"));
        if (approved)
            memberService.registerAsTeamOwner(requestingMemberId, request.getRequestedTeamName());
        teamOwnerRegRequestRepository.deleteById(requestingMemberId);
    }
}
