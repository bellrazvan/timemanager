package com.time.timemanager.mail;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.apache.logging.log4j.Logger;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;


@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${mail.from}")
    private String from;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private static final Logger LOG = LogManager.getLogger(EmailService.class);

    public void sendPasswordResetEmail(String to, String name, String resetLink) {
        final Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("resetLink", resetLink);

        final String emailContent = this.templateEngine.process("reset-password", context);
        final MimeMessage message = this.javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom(new InternetAddress(this.from, "Time Management App"));
            helper.setSubject("Password Reset - Time Management App");
            helper.setText(emailContent, true);
            this.javaMailSender.send(message);

            LOG.info("Password reset email successfully sent to user with email: " + to);
        } catch (MessagingException | MailException | UnsupportedEncodingException e) {
            LOG.error("Error sending the password reset email: " + e.getMessage());
        }
    }
}
