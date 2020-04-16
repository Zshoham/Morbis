package com.morbis.service;

import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberComplaint;
import com.morbis.model.member.entity.TeamManager;
import com.morbis.model.member.entity.TeamOwner;
import com.morbis.model.member.repository.MemberComplaintRepository;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.repository.TeamRepository;

import java.util.List;
import java.util.Optional;

public class AdminService {
    private TeamRepository teamRepository;
    private MemberRepository memberRepository;
    private MemberComplaintRepository memberComplaintRepository;

    public AdminService(TeamRepository teamRepository, MemberRepository memberRepository, MemberComplaintRepository memberComplaintRepository) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.memberComplaintRepository = memberComplaintRepository;
    }
    /**
     * @param teamID - ID of the team.
     * @return true if the team has been deleted, false if not.
     *  in addition, send's notifications to the owners and managers of the team
     */
    public void deleteTeam(int teamID) {
        //verify Team exists
        Optional<Team> team = teamRepository.findById(teamID);
        if(team.isEmpty()){
            return;
        }

        //send notifications to the team owners:
        List<TeamOwner> owners = team.get().getOwners();
        for (TeamOwner teamOwner :owners) {
            // teamOwner.sendMessage("the Team" + this.teamRepository.getOne(teamID).getName() + " has been deleted");
        }

        //send notifications to team managers:
        List<TeamManager> managers = team.get().getManagers();
        for (TeamManager teamManager : managers) {
            //teamManager.sendMassage("the Team" + this.teamRepository.getOne(teamID).getName() + " has been deleted");
        }

        //delete the Team:
        teamRepository.deleteById(teamID);
    }

    /**
     * @param memberID - ID of the member.
     * @return true if the member has been deleted, false if not.
     */
    public void deleteMember(int memberID) {
        this.memberRepository.deleteById(memberID);
    }

    /**
     * @return list of member complaints.
     */
    public List<MemberComplaint> getComplaints() {
        return this.memberComplaintRepository.findAll();
    }

    /**
     * @param memeberID - member ID
     * @param feedback  - message to give to the member.
     * @return
     */
    public boolean sendFeedback(int memeberID, String feedback) {
        //didnt finished
        Optional<Member> targetMember = this.memberRepository.findById(memeberID);
        if(targetMember.isEmpty()){
            return false;
        }
        targetMember.get().getEmail();





        return true;
    }

    public List<String> showLogFile() {
        return null;
    }

}
