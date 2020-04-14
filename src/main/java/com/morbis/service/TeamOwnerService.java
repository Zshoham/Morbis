package com.morbis.service;

import com.morbis.model.game.entity.Game;
import com.morbis.model.member.entity.*;
import com.morbis.model.member.repository.*;
import com.morbis.model.team.entity.Stadium;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.entity.TeamStatus;
import com.morbis.model.team.entity.Transaction;
import com.morbis.model.team.repository.StadiumRepository;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.model.team.repository.TransactionRepository;
import com.morbis.service.viewable.Asset;
import com.morbis.service.viewable.SearchResult;
import com.morbis.service.viewable.ViewableEntityType;
import com.morbis.service.viewable.ViewableProperties;
import org.apache.catalina.Manager;

import java.util.LinkedList;
import java.util.List;
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

    public List<Asset> getAssets(int teamID) {
        List<Asset> teamAssets = new LinkedList<>();
        Team team;
        team = teamRepository.findById(teamID).get();
        team.getPlayers().stream().map(player ->
                teamAssets.add(new Asset(PLAYER, player.getId())));
        team.getCoaches().stream().map(coach ->
                teamAssets.add(new Asset(COACH, coach.getId())));
        teamAssets.add(new Asset(STADIUM, team.getStadium().getId()));
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

    public List<Member> getNonOwners() {
        return memberRepository.findAllByMemberRoleIsNot(MemberRole.TEAM_OWNER);
    }

    public void makeTeamOwner(int ownerID,List<Integer> memberIDs) {
        TeamOwner currentOwner = teamOwnerRepository.findById(ownerID).get();
        for(int memberID : memberIDs) {
            Member member = memberRepository.findById(memberID).get();
            //TODO: need to make a new Team Owner with same info but different username and password and mail it to the member
            TeamOwner newTeamOwner = new TeamOwner(member.getUsername(),member.getPassword(),member.getName(),member.getEmail(),currentOwner.getTeam());
            currentOwner.getAppointedOwners().add(newTeamOwner);
            teamOwnerRepository.save(newTeamOwner);
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

    public List<Member> getNonManagers() {
        return memberRepository.findAllByMemberRoleIsNot(MemberRole.TEAM_OWNER);
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
