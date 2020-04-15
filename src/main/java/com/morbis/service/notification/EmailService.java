package com.morbis.service.notification;

import com.morbis.model.member.entity.Referee;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void registerReferee(Referee referee) {
        throw new UnsupportedOperationException("this method wasnt implemented yet");
    }

    public void closeTeam() {
        throw new UnsupportedOperationException("this method wasnt implemented yet");
    }

}
