package com.morbis.service;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.morbis.service.viewable.ViewableEntityType.*;

public class TeamOwnerService {
    private final TeamOwnerRepository teamOwnerRepository;
    private final TeamManagerRepository teamManagerRepository;
    private final PlayerRepository playerRepository;
    private final StadiumRepository stadiumRepository;
    private final CoachRepository coachRepository;
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final TransactionRepository transactionRepository;
    private final Logger logger;


    public TeamOwnerService(TeamOwnerRepository teamOwnerRepository, TeamManagerRepository teamManagerRepository, PlayerRepository playerRepository, StadiumRepository stadiumRepository, CoachRepository coachRepository, TeamRepository teamRepository, MemberRepository memberRepository, TransactionRepository transactionRepository) {
        this.teamOwnerRepository = teamOwnerRepository;
        this.teamManagerRepository = teamManagerRepository;
        this.playerRepository = playerRepository;
        this.stadiumRepository = stadiumRepository;
        this.coachRepository = coachRepository;
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.transactionRepository = transactionRepository;
        this.logger = LoggerFactory.getLogger(TeamOwnerService.class);
    }

    public List<Asset<?>> getTeamlessAssets() {
        logger.trace("called function: RefereeService->getTeamlessAssets.");
        List<Asset<?>> teamlessAssets = new LinkedList<>();
        for(Player player : playerRepository.findAllByTeamIsNull()) {
            if(player.getTeam()==null){
                teamlessAssets.add(new Asset<Player>(PLAYER,player.getId()));
            }
        }
        for(Coach coach : coachRepository.findAllByTeamIsNull()) {
            if(coach.getTeam()==null) {
                teamlessAssets.add(new Asset<Coach>(COACH, coach.getId()));
            }
        }
        for(Stadium stadium : stadiumRepository.findAllByTeamIsNull()) {
            if(stadium.getTeam()==null) {
                teamlessAssets.add(new Asset<Stadium>(STADIUM, stadium.getId()));
            }
        }
        return teamlessAssets;
    }

    public List<Asset<?>> getAssets(int teamID) {
        logger.trace("called function: RefereeService->getAssets. with team ID of: " + teamID);

        List<Asset<?>> teamAssets = new LinkedList<>();
        Optional<Team> team = teamRepository.findById(teamID);
        if(team.isEmpty())
            return teamAssets;
        team.get().getPlayers().forEach(player ->
                teamAssets.add(new Asset<Player>(PLAYER, player.getId())));
        team.get().getCoaches().forEach(coach ->
                teamAssets.add(new Asset<Coach>(COACH, coach.getId())));
        team.get().getManagers().forEach(teamManager ->
                teamAssets.add(new Asset<TeamManager>(TEAM_MANAGER, teamManager.getId())));
        teamAssets.add(new Asset<Stadium>(STADIUM, team.get().getStadium().getId()));
        return teamAssets;
    }

    public void addAssets(int teamID, List<Asset<?>> assets) {
        logger.trace("called function: RefereeService->addAssets. to team with the ID of: " + teamID);

        Optional<Team> team = teamRepository.findById(teamID);
        if(team.isEmpty())
            return;
        for (Asset<?> asset : assets) {
            switch (asset.getType()) {
                case COACH:
                    Coach coach = (Coach) asset.getAsset();
                    coach.setTeam(team.get());
                    team.get().getCoaches().add(coach);
                    coachRepository.save(coach);
                    break;
                case PLAYER:
                    Player player = (Player) asset.getAsset();
                    player.setTeam(team.get());
                    team.get().getPlayers().add(player);
                    playerRepository.save(player);
                    break;
                case STADIUM:
                    //TODO: need to add the option that team can have many stadiums and not only 1
                    Stadium stadium = (Stadium) asset.getAsset();
                    stadium.setTeam(team.get());
                    team.get().setStadium(stadium);
                    stadiumRepository.save(stadium);
                    break;
            }
        }
        teamRepository.save(team.get());
    }

    public void removeAssets(int teamID, List<Asset<?>> assets) {
        logger.trace("called function: RefereeService->removeAssets. to team with the ID of: " + teamID);
        Team team = teamRepository.findById(teamID).orElseThrow(
                () -> new IllegalArgumentException("invalid team id"));

        for (Asset<?> asset : assets) {
            switch (asset.getType()) {
                case COACH:
                    Coach coach = (Coach) asset.getAsset();
                    coach.setTeam(null);
                    team.getCoaches().removeIf(currentCoach -> currentCoach.getId() == coach.getId());
                    coachRepository.save(coach);
                    break;
                case PLAYER:
                    Player player = (Player) asset.getAsset();
                    player.setTeam(null);
                    team.getPlayers().removeIf(currentPlayer -> currentPlayer.getId() == player.getId());
                    playerRepository.save(player);
                    break;
                case STADIUM:
                    //TODO: need to add the option that team can have many stadiums and not only 1
                    Stadium stadium = (Stadium) asset.getAsset();
                    stadium.setTeam(null);
                    team.setStadium(null);
                    stadiumRepository.save(stadium);
                    break;
            }
        }
        teamRepository.save(team);
    }

    public void updateAsset(Asset<?> asset) {
        logger.trace("called function: RefereeService->updateAsset.");
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
        logger.trace("called function: RefereeService->getPossibleOwners.");
        List<MemberRole> roles = new LinkedList<>();
        roles.add(MemberRole.ASSOCIATION_REP);
        roles.add(MemberRole.ADMIN);
        roles.add(MemberRole.TEAM_OWNER);
        return memberRepository.findAllByMemberRoleNotIn(roles);
    }

    public void makeTeamOwner(int ownerID, List<Integer> memberIDs) {
        logger.trace("called function: RefereeService->makeTeamOwner. owner ID: " + ownerID);

        TeamOwner currentOwner = teamOwnerRepository.findById(ownerID).orElseThrow(
                () -> new IllegalArgumentException("invalid owner id")
        );

        List<Integer> possibleOwners = new LinkedList<>();
        getPossibleOwners().forEach(member -> possibleOwners.add(member.getId()));
        memberIDs.retainAll(possibleOwners);//to check if every1 can be owners
        for(Member member : memberRepository.findAllById(memberIDs)) {
            if(member.getMemberRole().contains(MemberRole.TEAM_MANAGER)) { // if he's a team manager we delete its team manager role
                teamManagerRepository.deleteById(member.getId());
                member.getMemberRole().remove(MemberRole.TEAM_MANAGER);
            }
            TeamOwner newTeamOwner = TeamOwner.newTeamOwner()
                    .fromMember(member)
                    .withTeam(currentOwner.getTeam())
                    .build();
            member.getMemberRole().add(MemberRole.TEAM_OWNER);
            currentOwner.getAppointedOwners().add(newTeamOwner);
            currentOwner.getTeam().getOwners().add(newTeamOwner);
            teamOwnerRepository.save(newTeamOwner);//save the new team owner
            memberRepository.save(member);

        }
        teamOwnerRepository.save(currentOwner);//save current owner's appointed owners
    }

    public List<TeamOwner> getAppointedOwners(int ownerID) {
        logger.trace("called function: RefereeService->getAppointedOwners. owner ID: " + ownerID);
        Optional<TeamOwner> currentOwner = teamOwnerRepository.findById(ownerID);
        if(currentOwner.isEmpty())
            return new LinkedList<>();
        return currentOwner.get().getAppointedOwners();
    }

    public void removeOwners(List<Integer> ownersIDs) {
        logger.trace("called function: RefereeService->removeOwners. owner ID's:" + ownersIDs.toString());
        for(int ownerID : ownersIDs) {
            Optional<TeamOwner> owner = teamOwnerRepository.findById(ownerID);
            if(owner.isEmpty())
                continue;
            List<Integer> appointedOwnersIDs = new LinkedList<>();
            //we go on all appointed owners and send them again to remove their appointed too.
            List<TeamOwner> appointedOwners = owner.get().getAppointedOwners();
            if(appointedOwners != null) {
                for(TeamOwner appointedOwner : owner.get().getAppointedOwners()) {
                    appointedOwnersIDs.add(appointedOwner.getId());
                }
                if(appointedOwnersIDs.size() > 0) {//we stop when we get to an owner who don't appoint other owners
                    removeOwners(appointedOwnersIDs);
                }
            }
            owner.get().getMemberRole().remove(MemberRole.TEAM_OWNER);
            teamOwnerRepository.deleteById(ownerID);
        }
    }

    public List<Member> getPossibleManagers() {
        logger.trace("called function: RefereeService->getPossibleManagers.");
        List<MemberRole> roles = new LinkedList<>();
        roles.add(MemberRole.ASSOCIATION_REP);
        roles.add(MemberRole.ADMIN);
        roles.add(MemberRole.TEAM_OWNER);
        roles.add(MemberRole.TEAM_MANAGER);
        return memberRepository.findAllByMemberRoleNotIn(roles);
    }

    public void makeTeamManager(int ownerID,List<Integer> memberIDs, List<List<ManagerPermissions>> permissions) {
        logger.trace("called function: RefereeService->makeTeamManager. Owner ID: " + ownerID + "memberIDs: " + memberIDs.toString());

        int counter = 0;
        Optional<TeamOwner> currentOwner = teamOwnerRepository.findById(ownerID);
        if(currentOwner.isEmpty())
            return;
        List<Integer> possibleManagers = new LinkedList<>();
        getPossibleManagers().forEach(member -> possibleManagers.add(member.getId()));
        memberIDs.retainAll(possibleManagers);//to check if every1 can be owners
        for(Member member : memberRepository.findAllById(memberIDs)) {
            TeamManager newTeamManager = new TeamManager(member.getId(),member.getUsername(),member.getPassword(),member.getName(),member.getEmail(),permissions.get(counter),currentOwner.get().getTeam());
            currentOwner.get().getAppointedManagers().add(newTeamManager);
            currentOwner.get().getTeam().getManagers().add(newTeamManager);
            member.getMemberRole().add(MemberRole.TEAM_MANAGER);
            teamManagerRepository.save(newTeamManager);
            memberRepository.save(member);
            counter++;
        }
        teamOwnerRepository.save(currentOwner.get());
    }

    public List<TeamManager> getAppointedManagers(int ownerID) {
        logger.trace("called function: RefereeService->getAppointedManagers. Owner ID: " + ownerID);
        Optional<TeamOwner> currentOwner = teamOwnerRepository.findById(ownerID);
        if(currentOwner.isEmpty())
            return new LinkedList<>();
        return currentOwner.get().getAppointedManagers();
    }

    public void removeManagers(List<Integer> managerIDs) {
        logger.trace("called function: RefereeService->removeManagers. manager IDs: " + managerIDs.toString());
        List<TeamManager> managers = teamManagerRepository.findAllById(managerIDs);
        teamManagerRepository.deleteAll(managers);
    }

    public void closeTeam(int teamID) {
        logger.trace("called function: RefereeService->closeTeam. teamID: " + teamID);
        Team team = teamRepository.findById(teamID).orElseThrow(
                () -> new IllegalArgumentException("invalid team id"));

        if(team.getTeamStatus() != TeamStatus.PERMANENTLY_CLOSED) {
            team.setTeamStatus(TeamStatus.TEMPORARY_CLOSED);
        }
    }

    public void openTeam(int teamID) {
        logger.trace("called function: RefereeService->openTeam. teamID: " + teamID);
        Team team = teamRepository.findById(teamID).orElseThrow(
                () -> new IllegalArgumentException("invalid team id"));
        if(team.getTeamStatus() != TeamStatus.PERMANENTLY_CLOSED) {
            team.setTeamStatus(TeamStatus.OPENED);
        }
    }

    public void setPermissions(int managerID, List<ManagerPermissions> permissions) {
        logger.trace("called function: RefereeService->setPermissions. manager ID: " + managerID);
        teamManagerRepository.findById(managerID).ifPresent(
                manager -> manager.setPermissions(permissions));
    }

    public void submitTransaction(int teamID, String description, int value) {
        logger.trace("called function: RefereeService->submitTransaction. team ID: " + teamID);
        Team team = teamRepository.findById(teamID).orElseThrow(
                () -> new IllegalArgumentException("invalid team id"));

        Transaction transaction = new Transaction(value,description);
        if(team.getTransactions() == null) {
           team.setTransactions(new LinkedList<>());
        }
        team.getTransactions().add(transaction);
        teamRepository.save(team);
        transactionRepository.save(transaction);
    }


}
