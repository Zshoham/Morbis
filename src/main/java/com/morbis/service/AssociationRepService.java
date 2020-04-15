package com.morbis.service;

import com.morbis.model.league.entity.League;
import com.morbis.model.league.entity.SchedulingMethod;
import com.morbis.model.league.entity.ScoringMethod;
import com.morbis.model.league.entity.Season;
import com.morbis.model.league.repository.LeagueRepository;
import com.morbis.model.league.repository.SeasonRepository;
import com.morbis.model.member.entity.Referee;
import com.morbis.model.member.repository.RefereeRepository;
import com.morbis.service.notification.EmailService;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AssociationRepService {

    private final SeasonRepository seasonRepository;
    private final LeagueRepository leagueRepository;
    private final RefereeRepository refereeRepository;

    private final EmailService emailService;

    public AssociationRepService(SeasonRepository seasonRepository,
                                 LeagueRepository leagueRepository,
                                 RefereeRepository refereeRepository,
                                 EmailService emailService) {
        this.seasonRepository = seasonRepository;
        this.leagueRepository = leagueRepository;
        this.refereeRepository = refereeRepository;
        this.emailService = emailService;
    }


    public boolean addLeague(String leagueName) {
        if (leagueRepository.findAllByName(leagueName).isEmpty()) {
            leagueRepository.save(new League(leagueName));
            return true;
        }

        return false;
    }

    public void addSeason(int leagueID, int year) throws IllegalArgumentException {
        League league = leagueRepository.findById(leagueID)
                .orElseThrow(() -> new IllegalArgumentException("trying to add season to non existent league"));

        if (seasonRepository.findByLeagueAndYear(league, year).isPresent())
            throw new IllegalArgumentException("the league " + league.getName() + "already contains the " + year + "season.");

        Season newSeason = new Season(year, league);
        seasonRepository.save(newSeason);
        league.getSeasons().add(newSeason);
        leagueRepository.save(league);
    }

    public List<League> getLeagues() {
        return leagueRepository.findAll();
    }

    public List<Season> getSeasons(int leagueID) {
        List<Season> result = new ArrayList<>();

        leagueRepository.findById(leagueID).ifPresent(league ->
                result.addAll(league.getSeasons()));

        return result;
    }

    public List<ScoringMethod> getScoringMethods() {
        return Stream.of(ScoringMethod.values()).collect(Collectors.toList());
    }

    public void setScoringMethod(int leagueID, ScoringMethod method) throws IllegalArgumentException {
        League league = leagueRepository.findById(leagueID)
                .orElseThrow(() -> new IllegalArgumentException("trying to add season to non existent league"));

        league.setScoringMethod(method);
        leagueRepository.save(league);
    }

    public List<SchedulingMethod> getSchedulingMethod() {
        return Stream.of(SchedulingMethod.values()).collect(Collectors.toList());
    }

    public void setSchedulingMethod(int leagueID, SchedulingMethod method) throws IllegalArgumentException {
        League league = leagueRepository.findById(leagueID)
                .orElseThrow(() -> new IllegalArgumentException("trying to add season to non existent league"));

        league.setSchedulingMethod(method);
        leagueRepository.save(league);
    }

    public void addRef(String email, String name) {
        String tempUsername = UUID.randomUUID().toString();
        String tempPassword = UUID.randomUUID().toString();

        Referee newAccount = Referee.newReferee("NA")
                .fromMember(tempUsername, tempPassword, name, email)
                .build();

        newAccount = refereeRepository.save(newAccount);

        emailService.registerReferee(newAccount);
    }

    //TODO: change diagram to return ref not id.
    public List<Referee> getRefs() {
        return refereeRepository.findAll();
    }

    //TODO: change diagram to accept ref and not id
    //TODO: change diagram, add refs to season connection (many to many).
    public void addRefsToSeason(int chosenSeasonID, List<Referee> referees) {
        Season chosen = seasonRepository.findById(chosenSeasonID).orElseThrow(
                () -> new IllegalArgumentException("the chosen season does not exist.")
        );

        chosen.getReferees().addAll(referees);
        seasonRepository.save(chosen);
    }

    //TODO: change diagram, add season to ref (many to many).
    //TODO: change diagram to accept Ref not id.
    public void removeRefs(List<Integer> refIDs) {
        List<Referee> referees = refereeRepository.findAllById(refIDs);

        for (Referee ref : referees) {
            for (Season season : ref.getSeasons()) {
                season.getReferees().remove(ref);
                seasonRepository.save(season);
            }
        }

        referees.forEach(refereeRepository::delete);
    }
}
