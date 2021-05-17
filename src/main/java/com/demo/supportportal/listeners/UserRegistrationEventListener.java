package com.demo.supportportal.listeners;

import com.demo.supportportal.events.UserRegistrationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class UserRegistrationEventListener implements ApplicationListener<UserRegistrationEvent> {

    private final String ACCOUNT_CREATION_SUCCESS_MESSAGE = "Hi %s,\n\nYour temporary password is: %s\n\nThanking you,\nSupport Portal\n";
    private JavaMailSender javaMailSender;

    @Autowired
    public UserRegistrationEventListener(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void onApplicationEvent(UserRegistrationEvent userRegistrationEvent) {

        // Initialize message
        String recipientEmail = userRegistrationEvent.getEmail();
        String subject = "Account credentials";
        String message = String.format(
                ACCOUNT_CREATION_SUCCESS_MESSAGE,
                userRegistrationEvent.getFirstName(),
                userRegistrationEvent.getPassword());

        // Send email
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(recipientEmail);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);
        simpleMailMessage.setSentDate(new Date());
        javaMailSender.send(simpleMailMessage);

    }
}
