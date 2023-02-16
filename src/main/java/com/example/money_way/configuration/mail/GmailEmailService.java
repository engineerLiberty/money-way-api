package com.example.money_way.configuration.mail;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GmailEmailService implements EmailService {
  private static final Logger LOGGER = LoggerFactory.getLogger(GmailEmailService.class);

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void sendEmail(String to, String subject, String messageContent) {

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("${spring.mail.username}");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(messageContent);

        mailSender.send(simpleMailMessage);

        LOGGER.info(String.format("Email Sent to -> %s", to));
    }
}



