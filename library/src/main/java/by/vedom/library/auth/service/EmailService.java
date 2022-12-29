package by.vedom.library.auth.service;


import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.Future;

@Service
@Log
public class EmailService {

    @Value("${client.url}")
    private String clientUrl;

    @Value("${email.from}")
    private String emailFrom;

    private final JavaMailSender sender;

    @Autowired
    public EmailService(JavaMailSender sender) {
        this.sender = sender;
    }

    @Async
    public Future<Boolean> sendActivationEmail(String email, String username, String uuid) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            String url = clientUrl + "/activate-account/" + uuid;

            String htmlMsg = String.format(
                    "Hi.<br/><br/>" +
                            "You have created an account for web-application \"To-do planner\": %s <br/><br/>" +
                            "<a href='%s'>%s</a><br/><br/>",  username, url, "Please click to this link to confirm your registration"
            );

            mimeMessage.setContent(htmlMsg, "text/html");
            message.setTo(email);
            message.setFrom(emailFrom);
            message.setSubject("Account activation is needed");
            message.setText(htmlMsg, true);
            sender.send(mimeMessage);
            return new AsyncResult<>(true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(false);
    }

    @Async
    public Future<Boolean> sendResetPasswordEmail(String email, String token) {
        try {
            MimeMessage mimeMessage = sender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            String url = clientUrl + "/update-password/" + token;

            String htmlMsg = String.format(
                    "Здравствуйте.<br/><br/>" +
                            "Someone requested password reset for web-application \"To-do planner\".<br/><br/>" +
                            "If it wasn't you please delete this letter.<br/><br/> Click the link below if you want to reset password: <br/><br/> " +
                            "<a href='%s'>%s</a><br/><br/>", url, "Reset password");

            mimeMessage.setContent(htmlMsg, "text/html");
            message.setTo(email);
            message.setSubject("Password reset");
            message.setFrom(emailFrom);
            message.setText(htmlMsg, true);
            sender.send(mimeMessage);
            return new AsyncResult<>(true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>(false);
    }
}
