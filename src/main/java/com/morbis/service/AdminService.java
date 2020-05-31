package com.morbis.service;

import com.morbis.model.member.entity.Member;
import com.morbis.model.member.entity.MemberComplaint;
import com.morbis.model.member.repository.MemberComplaintRepository;
import com.morbis.model.member.repository.MemberRepository;
import com.morbis.model.team.entity.Team;
import com.morbis.model.team.entity.TeamStatus;
import com.morbis.model.team.repository.TeamRepository;
import com.morbis.service.notification.EmailService;

import java.io.IOException;
import java.nio.file.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AdminService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;
    private final MemberComplaintRepository memberComplaintRepository;
    private final EmailService emailService;
    private final Logger logger;

    @Value("${logging.file")
    private String logFilePath;

    public AdminService(TeamRepository teamRepository, MemberRepository memberRepository, MemberComplaintRepository memberComplaintRepository, EmailService emailService) {
        this.teamRepository = teamRepository;
        this.memberRepository = memberRepository;
        this.memberComplaintRepository = memberComplaintRepository;
        this.emailService = emailService;
        this.logger = LoggerFactory.getLogger(AdminService.class);
    }

    /**
     * @param teamID - ID of the team.
     * @return true if the team has been deleted, false if not.
     * in addition, send's notifications to the owners and managers of the team
     */
    public void deleteTeam(int teamID) {
        logger.trace("called function: AdminService->deleteTeam. with the TeamID of: " + teamID + ".");
        //verify Team exists
        Optional<Team> team = teamRepository.findById(teamID);
        if (team.isEmpty()) {
            logger.warn("called function: AdminService->deleteTeam. teamID: " + teamID + " not belongs to any team.");
            return;
        }
        try {
            emailService.closeTeam(team.get());
        } catch (MessagingException e) {
            logger.error("Error: teamID: " + teamID + " email messages to the team owners and managers.");
            return;
        }

        //Terminate the Team:
        team.get().setTeamStatus(TeamStatus.PERMANENTLY_CLOSED);
        logger.info("Team " + team.get().getName() + " with the ID of: " + teamID + " has been Terminated.");
    }

    /**
     * @param memberID - ID of the member.
     * @return true if the member has been deleted, false if not.
     */
    public void deleteMember(int memberID) {
        logger.trace("called function: AdminService->deleteMember. with the ID of:" + memberID + ".");
        this.memberRepository.deleteById(memberID);
    }

    /**
     * @return list of member complaints.
     */
    public List<MemberComplaint> getComplaints() {
        logger.trace("called function: AdminService->getComplaints.");
        return this.memberComplaintRepository.findAll();
    }

    /**
     * @param memberID - member ID
     * @param feedback - message to give to the member.
     * @return
     */
    public boolean sendFeedback(int memberID, String feedback, MemberComplaint complaint) {
        logger.trace("called function: AdminService->sendFeedback. with the memberID: " + memberID);
        Optional<Member> targetMember = this.memberRepository.findById(memberID);
        if (targetMember.isEmpty()) {
            logger.warn("the target memberID" + memberID + " not belongs to any member.");
            return false;
        }
        try {
            emailService.sendMessage(targetMember.get().getEmail(),
                    "feedback for complaint on Post: " + complaint.getPost().getTitle() + ".", feedback);
        } catch (MessagingException e) {
            logger.error("Error: feedback from Admin to Member.");
            return false;
        }
        logger.info("feedback(mail) from Admin To: " + targetMember.get().getName() + " with the ID of: " + memberID + " has been sent.");
        return true;
    }

    public String showLogFile() {
        logger.trace("called function: AdminService->showLogFile.");
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(logFilePath)));
        } catch (IOException e) {
            logger.error("Error: showLogFile.");
        }
        return data;
    }
}
