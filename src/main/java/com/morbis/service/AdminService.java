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
    private boolean deleteTeam(int teamID) {
        //send notifications to the team owners:
        List<TeamOwner> owners =  this.teamRepository.getOne(teamID).getOwners();
        for (TeamOwner teamOwner :owners) {
            // teamOwner.sendMessage("the Team" + this.teamRepository.getOne(teamID).getName() + " has been deleted");
        }

        //send notifications to team managers:
        List<TeamManager> managers = this.teamRepository.getOne(teamID).getManagers();
        for (TeamManager teamManager : managers) {
            //teamManager.sendMassage("the Team" + this.teamRepository.getOne(teamID).getName() + " has been deleted");
        }

        //delete the Team:
        this.teamRepository.deleteById(teamID);
        try {
            Team deletedTeam = this.teamRepository.getOne(teamID);
            deletedTeam.getName();
        } catch (Exception e) {
            System.out.println("the Team has been deleted!");
            return true;
        }
        System.out.println("the Team hasn't been deleted!");
        return false;
    }

    /**
     * @param memberID - ID of the member.
     * @return true if the member has been deleted, false if not.
     */
    private boolean deleteMember(int memberID) {
        this.memberRepository.deleteById(memberID);
        try {
            Member deletedMember = this.memberRepository.getOne(memberID);
            deletedMember.getEmail();
        } catch (Exception e) {
            System.out.println("the member has been deleted!");
            return true;
        }
        System.out.println("the member hasn't been deleted");
        return false;
    }

    /**
     * @return list of member complaints.
     */
    private List<MemberComplaint> getComplaints() {
        return this.memberComplaintRepository.findAll();
    }

    /**
     * @param memeberID - member ID
     * @param feedback  - message to give to the member.
     * @return
     */
    private boolean sendFeedback(int memeberID, String feedback) {
        //didnt finished
        Member targetMember = this.memberRepository.getOne(memeberID);
        targetMember.getEmail();
        return false;

    }

    private List<String> showLogFile() {
        return null;
    }

}
