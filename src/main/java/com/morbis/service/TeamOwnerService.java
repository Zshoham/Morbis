package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.member.entity.*;
import com.morbis.model.member.repository.*;
import com.morbis.model.poster.entity.PosterData;
import com.morbis.model.team.entity.Stadium;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.entity.TeamStatus;
import com.morbis.model.team.entity.Transaction;
import com.morbis.model.team.repository.StadiumRepository;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.model.team.repository.TransactionRepository;
import com.morbis.service.randomString.RandomStringGenerator;
import com.morbis.service.viewable.Asset;
import com.morbis.service.viewable.SearchResult;
import com.morbis.service.viewable.ViewableEntityType;
import com.morbis.service.viewable.ViewableProperties;
import org.apache.catalina.Manager;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.morbis.service.viewable.ViewableEntityType.*;

public class TeamOwnerService {
    private TeamOwnerRepository teamOwnerRepository;
    private TeamManagerRepository teamManagerRepository;
    private PlayerRepository playerRepository;
    private StadiumRepository stadiumRepository;
    private CoachRepository coachRepository;
    private TeamRepository teamRepository;
    private MemberRepository memberRepository;
    private TransactionRepository transactionRepository;

    public TeamOwnerService(TeamOwnerRepository teamOwnerRepository, TeamManagerRepository teamManagerRepository, PlayerRepository playerRepository, StadiumRepository stadiumRepository, CoachRepository coachRepository, TeamRepository teamRepository, MemberRepository memberRepository, TransactionRepository transactionRepository) {
        this.teamOwnerRepository = teamOwnerRepository;
        this.teamManagerRepository = teamManagerRepository;
        this.playerRepository = playerRepository;
        this.stadiumRepository = stadiumRepository;
        this.coachRepository = coachRepository;
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Asset> getTeamlessAssets() {
        List<Asset> teamlessAssets = new LinkedList<>();
        for(Player player : playerRepository.findAllByTeamIsNull()) {
            if(player.getTeam()==null){
                teamlessAssets.add(new Asset(PLAYER,player.getId()));
            }
        }
        for(Coach coach : coachRepository.findAllByTeamIsNull()) {
            if(coach.getTeam()==null) {
                teamlessAssets.add(new Asset(COACH, coach.getId()));
            }
        }
        for(Stadium stadium : stadiumRepository.findAllByTeamIsNull()) {
            if(stadium.getTeam()==null) {
                teamlessAssets.add(new Asset(STADIUM, stadium.getId()));
            }
        }
        return teamlessAssets;
    }

    public List<Asset<?>> getAssets(int teamID) {
        List<Asset<?>> teamAssets = new LinkedList<>();
        Optional<Team> team = teamRepository.findById(teamID);
        if(team.isEmpty())
            return teamAssets;
        team.get().getPlayers().forEach(player ->
                teamAssets.add(new Asset<Player>(PLAYER, player.getId())));
        team.get().getCoaches().forEach(coach ->
                teamAssets.add(new Asset<Coach>(COACH, coach.getId())));
        teamAssets.add(new Asset<Stadium>(STADIUM, team.get().getStadium().getId()));
        return teamAssets;
    }

    public void addAssets(int teamID, List<Asset> assets) {
        Team team = teamRepository.findById(teamID).get();
        for (Asset asset : assets) {
            switch (asset.getType()) {
                case COACH:
                    Coach coach = (Coach) asset.getAsset();
                    coach.setTeam(team);
                    team.getCoaches().add(coach);
                    coachRepository.save(coach);
                    break;
                case PLAYER:
                    Player player = (Player) asset.getAsset();
                    player.setTeam(team);
                    team.getPlayers().add(player);
                    playerRepository.save(player);
                    break;
                case STADIUM:
                    //TODO: need to add the option that team can have many stadiums and not only 1
                    Stadium stadium = (Stadium) asset.getAsset();
                    stadium.setTeam(team);
                    team.setStadium(stadium);
                    stadiumRepository.save(stadium);
                    break;
                default:
                    break;
            }
        }
        teamRepository.save(team);
    }

    public void removeAssets(int teamID, List<Asset> assets) {
        Team team = teamRepository.findById(teamID).get();
        for (Asset asset : assets) {
            switch (asset.getType()) {
                case COACH:
                    Coach coach = (Coach) asset.getAsset();
                    coach.setTeam(null);
                    team.getCoaches().remove(coach);
                    coachRepository.save(coach);
                    break;
                case PLAYER:
                    Player player = (Player) asset.getAsset();
                    player.setTeam(null);
                    team.getPlayers().remove(player);
                    playerRepository.save(player);
                    break;
                case STADIUM:
                    //TODO: need to add the option that team can have many stadiums and not only 1
                    Stadium stadium = (Stadium) asset.getAsset();
                    stadium.setTeam(null);
                    team.setStadium(null);
                    stadiumRepository.save(stadium);
                    break;
                default:
                    break;
            }
        }
        teamRepository.save(team);
    }

    public void updateAsset(Asset asset) {
        switch (asset.getType()) {
            case COACH:
                Coach coach = (Coach) asset.getAsset();
                coachRepository.save(coach);
                break;
            case PLAYER:
                Player player = (Player) asset.getAsset();
                playerRepository.save(player);
                break;
            case STADIUM:
                //TODO: need to add the option that team can have many stadiums and not only 1
                Stadium stadium = (Stadium) asset.getAsset();
                stadiumRepository.save(stadium);
                break;
            default:
                break;
        }
    }

    public List<Member> getPossibleOwners() {
        List<MemberRole> roles = new LinkedList<>();
        roles.add(MemberRole.ASSOCIATION_REP);
        roles.add(MemberRole.ADMIN);
        roles.add(MemberRole.TEAM_OWNER);
        roles.add(MemberRole.TEAM_MANAGER);
        List<Member> possibleOwners = memberRepository.findAllByMemberRoleNotIn(roles);
        List<String> possibleOwnersEmails = new LinkedList<>();
        possibleOwners.forEach(member ->
                possibleOwnersEmails.add(member.getEmail()));
        List<Member> existOwners = teamOwnerRepository.findAllByEmailIn(possibleOwnersEmails);
        possibleOwners.removeAll(existOwners);
        List<Member> existManagers = teamManagerRepository.findAllByEmailIn(possibleOwnersEmails);
        for(Member manager : existManagers) {
            if (possibleOwners.contains(manager)) {
                possibleOwners.remove(manager);
            }
            possibleOwners.add(manager);
        }
        return possibleOwners;
    }

    public void makeTeamOwner(int ownerID,List<Integer> memberIDs) {
        TeamOwner currentOwner = teamOwnerRepository.findById(ownerID).get();
        List<Integer> possibleOwners = new LinkedList<>();
        getPossibleOwners().forEach(member -> possibleOwners.add(member.getId()));
        memberIDs.retainAll(possibleOwners);//to check if every1 can be owners
        for(Member member : memberRepository.findAllById(memberIDs)) {
            String randomUsername = RandomStringGenerator.getRandomString();
            String randomPassword = RandomStringGenerator.getRandomString();
            while(memberRepository.existsMemberByUsername(randomUsername)) { //we generate again and again till we find something that is not taken
                randomUsername = RandomStringGenerator.getRandomString();
            }
            TeamOwner newTeamOwner = new TeamOwner(randomUsername,randomPassword,member.getName(),member.getEmail(),currentOwner.getTeam());
            currentOwner.getAppointedOwners().add(newTeamOwner);
            currentOwner.getTeam().getOwners().add(newTeamOwner);
            teamOwnerRepository.save(newTeamOwner);
            //TODO: send email to the member with the info we made
            if(member.getMemberRole() == MemberRole.TEAM_MANAGER) { // if he's a team manager we delete its team manager account
                teamManagerRepository.deleteById(member.getId());
            }
        }
        teamOwnerRepository.save(currentOwner);
    }

    public List<TeamOwner> getAppointedOwners(int ownerID) {
        TeamOwner currentOwner = teamOwnerRepository.findById(ownerID).get();
        return currentOwner.getAppointedOwners();
    }

    public void removeOwners(List<Integer> ownersIDs) {
        for(int ownerID : ownersIDs) {
            TeamOwner owner = teamOwnerRepository.findById(ownerID).get();
            List<Integer> appointedOwnersIDs = new LinkedList<>();
            //we go on all appointed owners and send them again to remove their appointed too.
            for(TeamOwner appointedOwner : owner.getAppointedOwners()) {
                appointedOwnersIDs.add(appointedOwner.getId());
            }
            if(appointedOwnersIDs.size() > 0) {//we stop when we get to an owner who don't appoint other owners
                removeOwners(appointedOwnersIDs);
            }
            teamOwnerRepository.deleteById(ownerID);
        }
    }

    public List<Member> getPossibleManagers() {
        List<MemberRole> roles = new LinkedList<>();
        roles.add(MemberRole.ASSOCIATION_REP);
        roles.add(MemberRole.ADMIN);
        roles.add(MemberRole.TEAM_OWNER);
        roles.add(MemberRole.TEAM_MANAGER);
        List<Member> possibleManagers = memberRepository.findAllByMemberRoleNotIn(roles);
        List<String> possibleManagersEmails = new LinkedList<>();
        possibleManagers.forEach(member ->
                possibleManagersEmails.add(member.getEmail()));
        List<Member> existOwners = teamOwnerRepository.findAllByEmailIn(possibleManagersEmails);
        List<Member> existManagers = teamManagerRepository.findAllByEmailIn(possibleManagersEmails);
        possibleManagers.removeAll(existOwners);
        possibleManagers.removeAll(existManagers);
        return possibleManagers;
    }

    public void makeTeamManager(int ownerID,List<Integer> memberIDs, List<List<ManagerPermissions>> permissions) {
        TeamOwner currentOwner = teamOwnerRepository.findById(ownerID).get();
        List<Integer> possibleManagers = new LinkedList<>();
        getPossibleOwners().forEach(member -> possibleManagers.add(member.getId()));
        memberIDs.retainAll(possibleManagers);//to check if every1 can be owners
        for(Member member : memberRepository.findAllById(memberIDs)) {
            String randomUsername = RandomStringGenerator.getRandomString();
            String randomPassword = RandomStringGenerator.getRandomString();
            while(memberRepository.existsMemberByUsername(randomUsername)) { //we generate again and again till we find something that is not taken
                randomUsername = RandomStringGenerator.getRandomString();
            }
            TeamManager newTeamManager = new TeamManager(randomUsername,randomPassword,member.getName(),member.getEmail(),permissions,currentOwner.getTeam());
            currentOwner.getAppointedManagers().add(newTeamManager);
            currentOwner.getTeam().getManagers().add(newTeamManager);
            teamManagerRepository.save(newTeamManager);
            //TODO: send email to the member with the info we made
        }
        teamOwnerRepository.save(currentOwner);
    }

    public List<TeamManager> getAppointedManagers(int ownerID) {
        TeamOwner currentOwner = teamOwnerRepository.findById(ownerID).get();
        return currentOwner.getAppointedManagers();
    }

    public void removeManagers(List<Integer> ownersIDs) {
        for(int ownerID : ownersIDs) {
            teamOwnerRepository.deleteById(ownerID);
        }
    }

    //TODO: need to add functions to remove/make some1 manager/owner


    public void closeTeam(int teamID) {
        Team team = teamRepository.findById(teamID).get();
        if(team.getTeamStatus() == TeamStatus.OPENED) {
            team.setTeamStatus(TeamStatus.TEMPORARY_CLOSED);
        }
    }

    public void openTeam(int teamID) {
        Team team = teamRepository.findById(teamID).get();
        if(team.getTeamStatus() == TeamStatus.TEMPORARY_CLOSED) {
            team.setTeamStatus(TeamStatus.OPENED);
        }
    }

    public void setPermissions(int managerID, List<ManagerPermissions> permissions) {
        TeamManager teamManager = teamManagerRepository.findById(managerID).get();
        teamManager.setPermissions(permissions);
    }

    public void submitTransaction(int teamID, String description, int value) {
        transactionRepository.save(new Transaction(value,description));
    }


}
