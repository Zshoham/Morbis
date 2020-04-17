package com.morbis.service;

import com.morbis.data.ViewableEntitySource;
import com.morbis.model.member.entity.*;
import com.morbis.model.member.repository.*;
import com.morbis.model.team.entity.Stadium;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.entity.TeamStatus;
import com.morbis.model.team.repository.StadiumRepository;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.model.team.repository.TransactionRepository;
import com.morbis.service.viewable.Asset;
import com.morbis.service.viewable.ViewableEntityType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.morbis.data.ViewableEntitySource.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TeamOwnerService.class)
public class TeamOwnerServiceTest {

    @Autowired
    private TeamOwnerService teamOwnerService;

    @MockBean private MemberRepository memberRepository;
    @MockBean private CoachRepository coachRepository;
    @MockBean private PlayerRepository playerRepository;
    @MockBean private TeamManagerRepository teamManagerRepository;
    @MockBean private StadiumRepository stadiumRepository;
    @MockBean private TeamRepository teamRepository;
    @MockBean private TeamOwnerRepository teamOwnerRepository;
    @MockBean private TransactionRepository transactionRepository;

    @Test
    public void getTeamlessAssets() {
                            // setup mock
        // initiallize player
        LinkedList<Player> teamlessPlayers = new LinkedList<>();
        Player testPlayer = new Player(1, "playerUser", "playerPass", "playerName", "playerEmail",LocalDateTime.now(),"Goal Keeper");
        teamlessPlayers.add(testPlayer);
        when(playerRepository.findAllByTeamIsNull()).thenReturn(teamlessPlayers);
        // initiallize stadium
        LinkedList<Stadium> teamlessStadiums = new LinkedList<>();
        Stadium testStadium = new Stadium(2, "Sami Ofer");
        teamlessStadiums.add(testStadium);
        when(stadiumRepository.findAllByTeamIsNull()).thenReturn(teamlessStadiums);
        // initiallize coach
        LinkedList<Coach> teamlessCoaches = new LinkedList<>();
        Coach testCoach = new Coach(3,"coachUser","coachPass" ,"coachName" ,"coachEmail" ,"Qualifications are for losers" ,"Manager Assistant");
        teamlessCoaches.add(testCoach);
        when(coachRepository.findAllByTeamIsNull()).thenReturn(teamlessCoaches);

        // test assets don't have teams
        List<Asset<?>> teamlessAssets = teamOwnerService.getTeamlessAssets();
        // verifies that every asset doesn't have a team
        assertThat(teamlessAssets)
                .hasSize(3);

        // test all assets have teams
        Team testTeam = new Team();
        testCoach.setTeam(testTeam);
        testStadium.setTeam(testTeam);
        testPlayer.setTeam(testTeam);
        teamlessAssets = teamOwnerService.getTeamlessAssets();
        assertThat(teamlessAssets)
                .hasSize(0);
    }

    public void setUp() {
        ViewableEntitySource.initWithID();
        List<MemberRole> chooseOwnerRoles = new LinkedList<>();
        chooseOwnerRoles.add(MemberRole.ASSOCIATION_REP);
        chooseOwnerRoles.add(MemberRole.ADMIN);
        chooseOwnerRoles.add(MemberRole.TEAM_OWNER);
        List<MemberRole> chooseManagerRoles = new LinkedList<>();
        chooseManagerRoles.add(MemberRole.ASSOCIATION_REP);
        chooseManagerRoles.add(MemberRole.ADMIN);
        chooseManagerRoles.add(MemberRole.TEAM_OWNER);
        chooseManagerRoles.add(MemberRole.TEAM_MANAGER);

        when(stadiumRepository.findById(1)).thenReturn(Optional.ofNullable(homeStadium));
        when(playerRepository.findById(3)).thenReturn(Optional.ofNullable(homePlayer));
        when(teamRepository.findById(11)).thenReturn(Optional.ofNullable(home));
        when(teamRepository.findById(12)).thenReturn(Optional.ofNullable(away));
        when(memberRepository.findAllByMemberRoleNotIn(chooseOwnerRoles)).thenReturn(List.of(homeCoach,homeManager,homePlayer));
        when(memberRepository.findAllByMemberRoleNotIn(chooseManagerRoles)).thenReturn(List.of(homeCoach,homePlayer));
        when(teamOwnerRepository.findById(5)).thenReturn(Optional.ofNullable(homeOwner));
        when(teamManagerRepository.findById(7)).thenReturn(Optional.ofNullable(homeManager));
        when(teamManagerRepository.findById(8)).thenReturn(Optional.of(awayManager));
        homeOwner.setAppointedOwners(new LinkedList<>());
        homeOwner.setAppointedManagers(new LinkedList<>());
        List<TeamOwner> teamOwners = new LinkedList<>();
        teamOwners.add(homeOwner);
        home.setOwners(teamOwners);
        List<TeamManager> teamManagers = new LinkedList<>();
        teamManagers.add(homeManager);
        home.setManagers(teamManagers);
        List<Player> players = new LinkedList<>();
        players.add(homePlayer);
        home.setPlayers(players);
        List<Coach> coaches = new LinkedList<>();
        coaches.add(homeCoach);
        home.setCoaches(coaches);
        when(memberRepository.findAllById(List.of(9,7,3))).thenReturn(List.of(homeCoach,homeManager,homePlayer));
        when(memberRepository.findAllById(List.of(9,3))).thenReturn(List.of(homeCoach,homePlayer));
        when(memberRepository.findAllById(List.of(9))).thenReturn(List.of(homeCoach));
        when(teamOwnerRepository.findById(5)).thenReturn(Optional.ofNullable(homeOwner));
    }

    @Test
    public void getAssets() {
        ViewableEntitySource.initWithID();
        when(teamRepository.findById(11)).thenReturn(Optional.ofNullable(home));
        when(teamRepository.findById(12)).thenReturn(Optional.ofNullable(away));
        List<Asset<?>> teamAssets;
        teamAssets = teamOwnerService.getAssets(11); //stadium,player,coach,manager
        assertThat(teamAssets)
                .hasSize(4);
        teamAssets = teamOwnerService.getAssets(12);
        assertThat(teamAssets)
                .hasSize(4);
    }

    @Test
    public void addAssets() {
        setUp();

        Coach testCoach = new Coach(100,"coachUser","coachPass" ,"coachName" ,"coachEmail" ,"Qualifications are for losers" ,"Manager Assistant");
        Player testPlayer = new Player(101, "playerUser", "playerPass", "playerName", "playerEmail",LocalDateTime.now(),"Goal Keeper");
        List<Asset<?>> newAssets = new LinkedList<>();
        Stadium testStadium = new Stadium(102, "Sami Ofer");
        Asset<Coach> assetCoach = new Asset<Coach>(ViewableEntityType.COACH,100);
        assetCoach.putRecord(testCoach);
        Asset<Player> assetPlayer = new Asset<Player>(ViewableEntityType.PLAYER,101);
        assetPlayer.putRecord(testPlayer);
        Asset<Stadium> assetStadium = new Asset<Stadium>(ViewableEntityType.STADIUM,102);
        assetStadium.putRecord(testStadium);
        newAssets.add(assetCoach);
        newAssets.add(assetPlayer);
        newAssets.add(assetStadium);
        teamOwnerService.addAssets(11,newAssets);
        assertThat(home.getCoaches())
                .hasSize(2);
        assertThat(home.getPlayers())
                .hasSize(2);
    }

    @Test
    public void removeAssets() {
        setUp();

        Asset<Coach> assetCoach = new Asset<Coach>(ViewableEntityType.COACH,9);
        assetCoach.putRecord(homeCoach);
        Asset<Player> assetPlayer = new Asset<Player>(ViewableEntityType.PLAYER,3);
        assetPlayer.putRecord(homePlayer);
        Asset<Stadium> assetStadium = new Asset<Stadium>(ViewableEntityType.STADIUM,1);
        assetStadium.putRecord(homeStadium);
        List<Asset<?>> assetsToRemove = new LinkedList<>();
        assetsToRemove.add(assetCoach);
        assetsToRemove.add(assetPlayer);
        assetsToRemove.add(assetStadium);
        teamOwnerService.removeAssets(home.getId() ,assetsToRemove);
        assertThat(home.getCoaches())
                .hasSize(0);
        assertThat(home.getPlayers())
                .hasSize(0);
        assertThat(home.getStadium())
                .isNull();
    }

    @Test
    public void updateAsset() {
        setUp();

        Coach updatedCoach = new Coach(9,"coachUser","coachPass" ,"coachName" ,"coachEmail" ,"Qualifications are for losers" ,"Manager Assistant");
        Asset<Coach> updatedAssetCoach = new Asset<Coach>(ViewableEntityType.COACH,9);
        updatedAssetCoach.putRecord(updatedCoach);
        teamOwnerService.updateAsset(updatedAssetCoach);
        Player updatedPlayer = new Player(3, "playerUser", "playerPass", "playerName", "playerEmail",LocalDateTime.now(),"Goal Keeper");
        Asset<Player> updatedAssetPlayer = new Asset<Player>(ViewableEntityType.PLAYER,9);
        updatedAssetPlayer.putRecord(updatedPlayer);
        teamOwnerService.updateAsset(updatedAssetPlayer);
        Stadium updatedStadium = new Stadium(2, "Sami Ofer");
        Asset<Stadium> updatedAssetStadium = new Asset<Stadium>(ViewableEntityType.STADIUM,9);
        updatedAssetStadium.putRecord(updatedStadium);
        teamOwnerService.updateAsset(updatedAssetStadium);
        //can't really check the function because it just save a temp to the database
    }

    @Test
    public void getPossibleOwners() {
        setUp();

        List<Member> possibleOwners = teamOwnerService.getPossibleOwners();
        assertThat(possibleOwners)
                .hasSize(3);
    }
    @Test
    public void makeTeamOwner() {
        setUp();

        List<Integer> memberIDs = new LinkedList<>();
        memberIDs.add(homeCoach.getId());
        memberIDs.add(homeManager.getId());
        memberIDs.add(homePlayer.getId());
        teamOwnerService.makeTeamOwner(5,memberIDs);
        assertThat(homeCoach.getMemberRole())
                .containsExactly(MemberRole.COACH,MemberRole.TEAM_OWNER);
    }

    @Test
    public void getAppointedOwners() {
        setUp();
        List<Integer> coach = new LinkedList<>();
        coach.add(homeCoach.getId());

        teamOwnerService.makeTeamOwner(5,coach);
        assertThat(teamOwnerService.getAppointedOwners(5)).hasSize(1);
    }

    @Test
    public void removeOwners() {
        setUp();

        TeamOwner teamOwner1 = new TeamOwner(0,"username","password","namee","email",home);
        TeamOwner teamOwner2 = new TeamOwner(100,"username2","password2","namee2","email2",home);
        List<TeamOwner> firstAppointedOwners = new LinkedList<>();
        firstAppointedOwners.add(teamOwner1);
        List<TeamOwner> secondAppointedOwners = new LinkedList<>();
        secondAppointedOwners.add(teamOwner2);
        homeOwner.setAppointedOwners(firstAppointedOwners);
        teamOwner1.setAppointedOwners(secondAppointedOwners);
        List<Integer> ownersIDs = new LinkedList<>();
        ownersIDs.add(5);
        when(teamOwnerRepository.findById(0)).thenReturn(Optional.of(teamOwner1));
        when(teamOwnerRepository.findById(100)).thenReturn(Optional.of(teamOwner2));
        teamOwnerService.removeOwners(ownersIDs);
        verify(teamOwnerRepository, times(1)).deleteById(teamOwner1.getId());
        verify(teamOwnerRepository, times(1)).deleteById(teamOwner2.getId());
        verify(teamOwnerRepository, times(1)).deleteById(homeOwner.getId());
    }

    @Test
    public void getPossibleManagers() {
        setUp();

        List<Member> possibleManagers = teamOwnerService.getPossibleManagers();
        assertThat(possibleManagers)
                .hasSize(2);
    }

    @Test
    public void makeTeamManager() {
        setUp();

        List<ManagerPermissions> coachPermissions = new LinkedList<>();
        coachPermissions.add(ManagerPermissions.CLOSE_TEAM);
        coachPermissions.add(ManagerPermissions.OPEN_TEAM);
        List<ManagerPermissions> playerPermissions = new LinkedList<>();
        playerPermissions.add(ManagerPermissions.ADD_ASSETS);
        playerPermissions.add(ManagerPermissions.UPDATE_ASSETS);
        List<List<ManagerPermissions>> managersPermissions = new LinkedList<>();
        managersPermissions.add(coachPermissions);
        managersPermissions.add(playerPermissions);
        List<Integer> memberIDs = new LinkedList<>();
        memberIDs.add(homeCoach.getId());
        memberIDs.add(homePlayer.getId());
        teamOwnerService.makeTeamManager(5,memberIDs,managersPermissions);
        assertThat(homeCoach.getMemberRole())
                .containsExactly(MemberRole.COACH,MemberRole.TEAM_MANAGER);
    }

    @Test
    public void getAppointedManagers() {
        setUp();

        List<Integer> coach = new LinkedList<>();
        coach.add(homeCoach.getId());
        List<ManagerPermissions> coachPermissions = new LinkedList<>();
        coachPermissions.add(ManagerPermissions.CLOSE_TEAM);
        coachPermissions.add(ManagerPermissions.OPEN_TEAM);
        List<List<ManagerPermissions>> managersPermissions = new LinkedList<>();
        managersPermissions.add(coachPermissions);

        teamOwnerService.makeTeamManager(5,coach,managersPermissions);
        assertThat(teamOwnerService.getAppointedManagers(5)).hasSize(1);
    }

    @Test
    public void removeMangers() {
        setUp();

        List<Integer> managersIDs = new LinkedList<>();
        managersIDs.add(7);
        managersIDs.add(8);
        teamOwnerService.removeManagers(managersIDs);
        verify(teamManagerRepository, times(1)).deleteById(homeManager.getId());
        verify(teamManagerRepository, times(1)).deleteById(awayManager.getId());
    }

    @Test
    public void closeTeam() {
        setUp();
        //close a team that is opened
        teamOwnerService.closeTeam(11);
        assertThat(home.getTeamStatus())
                .isEqualTo(TeamStatus.TEMPORARY_CLOSED);
        //close a team that is closed
        teamOwnerService.closeTeam(11);
        assertThat(home.getTeamStatus())
                .isEqualTo(TeamStatus.TEMPORARY_CLOSED);
    }

    @Test
    public void openTeam() {
        setUp();

        //if we open a team that is already opened
        teamOwnerService.openTeam(11);
        assertThat(home.getTeamStatus())
                .isEqualTo(TeamStatus.OPENED);
        //if we open a team that is closed
        home.setTeamStatus(TeamStatus.TEMPORARY_CLOSED);
        teamOwnerService.openTeam(11);
        assertThat(home.getTeamStatus())
                .isEqualTo(TeamStatus.OPENED);
    }

    @Test
    public void testSetPermissions() {
        setUp();

        List<ManagerPermissions> permissions = new LinkedList<>();
        permissions.add(ManagerPermissions.ADD_ASSETS);
        permissions.add(ManagerPermissions.CLOSE_TEAM);

        teamOwnerService.setPermissions(7,permissions);

        assertThat(homeManager.getPermissions())
                .containsExactly(ManagerPermissions.ADD_ASSETS,ManagerPermissions.CLOSE_TEAM);

    }

    @Test
    public void testSubmitTransaction() {
        setUp();

        teamOwnerService.submitTransaction(11,"Sold Messi to Real Madrid",300000000);
        assertThat(home.getTransactions())
                .hasSize(1);
    }
}
