package com.time.timemanager.mail;

import com.time.timemanager.tasks.dtos.TaskResponse;
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
    private String FROM;
    @Value("${frontend.url}")
    private String FRONTEND_URL;
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
    private static final String OVERDUE = "Overdue";
    private static final String DUE_TOMORROW = "Due Tomorrow";
    private static final String APP_NAME = "TimeManager";
    private static final String YOUR_TASK_IS = "Your Task is ";
    private static final String PASSWORD_RESET = "Password Reset";
    private static final String CONFIRM_YOUR_ACCOUNT = "Confirm your account";
    private static final String ACCOUNT_CONFIRMATION_PATH = "emails/confirmation";
    private static final String TASK_REMINDER_TEMPLATE_PATH = "emails/task-reminder";
    private static final String PASSWORD_RESET_TEMPLATE_PATH = "emails/reset-password";
    private static final String LOG_MESSAGE = "email successfully sent to user with email: ";
    private static final Logger LOG = LogManager.getLogger(EmailService.class);

    public void sendConfirmationEmail(String to, String name, String token) {
        final Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("confirmationLink", this.FRONTEND_URL + "/confirm-account?token=" + token);

        this.sendEmail(to,
                CONFIRM_YOUR_ACCOUNT + " - " + APP_NAME,
                ACCOUNT_CONFIRMATION_PATH,
                context,
                "Confirmation " + LOG_MESSAGE);
    }

    public void sendPasswordResetEmail(String to, String name, String token) {
        final Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("resetLink", this.FRONTEND_URL + "/reset-password?token=" + token);

        this.sendEmail(to,
                PASSWORD_RESET + " - " + APP_NAME,
                PASSWORD_RESET_TEMPLATE_PATH,
                context,
                PASSWORD_RESET + " " + LOG_MESSAGE);
    }

    public void sendReminderDueTomorrow(String to, String name, TaskResponse taskResponse) {
        final Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("task", taskResponse);
        context.setVariable("appUrl", this.FRONTEND_URL + "/tasks/" + taskResponse.id());
        context.setVariable("type", DUE_TOMORROW);

        this.sendEmail(to,
                YOUR_TASK_IS + DUE_TOMORROW + " - " + APP_NAME,
                TASK_REMINDER_TEMPLATE_PATH,
                context,
            "Task before due date " + LOG_MESSAGE);
    }

    public void sendReminderOverdue(String to, String name, TaskResponse taskResponse) {
        final Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("task", taskResponse);
        context.setVariable("appUrl", this.FRONTEND_URL + "/tasks/" + taskResponse.id());
        context.setVariable("type", OVERDUE);

        this.sendEmail(to,
                YOUR_TASK_IS + OVERDUE + " - " + APP_NAME,
                TASK_REMINDER_TEMPLATE_PATH,
                context,
            "Task overdue " + LOG_MESSAGE);
    }

    private void sendEmail(String to, String subject, String templateName, Context context, String logMessage) {
        final String emailContent = this.templateEngine.process(templateName, context);
        final MimeMessage message = this.javaMailSender.createMimeMessage();

        try {
            final MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom(new InternetAddress(this.FROM, APP_NAME));
            helper.setSubject(subject);
            helper.setText(emailContent, true);
            this.javaMailSender.send(message);
            LOG.info(logMessage + to);
        } catch (MessagingException | MailException | UnsupportedEncodingException e) {
            LOG.error("Error sending email: " + e.getMessage());
        }
    }
}
