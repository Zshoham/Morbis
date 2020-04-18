package com.morbis.service.notification;

import com.morbis.model.member.entity.Referee;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.util.Date;
import java.util.Properties;

@Service
public class EmailService {

    private final Properties props;

    @Value("${spring.mail.host.address")
    private String hostAddress;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    private static final String REF_MESSAGE =
            "Hello %s,\n" +
                    "You have been registered to the morbis system !\n" +
                    "Your new username is: %s\n" +
                    "Your new password is: %s\n" +
                    "Please change your username and password after the first login.";

    EmailService() {
        props= new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }



    public void sendMessage(String address, String subject, String content) throws MessagingException {
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(hostAddress, false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(address));
        msg.setSubject(subject);
        msg.setContent(content, "text/html");
        msg.setSentDate(Date.from(Instant.now()));

        Transport.send(msg);
    }

    public void registerReferee(Referee referee) throws MessagingException {
        sendMessage(referee.getEmail(),
                "Morbis - Referee Registration",
                String.format(REF_MESSAGE, referee.getName(), referee.getUsername(), referee.getPassword()));
    }

    public void closeTeam() {
        throw new UnsupportedOperationException("this method wasnt implemented yet");
    }

}
