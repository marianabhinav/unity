package com.amcrest.unity.accounting.email;

import com.amcrest.unity.accounting.email.token.EmailOtpService;
import com.amcrest.unity.accounting.user.domain.User;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import static org.springframework.util.StringUtils.capitalize;

@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final EmailOtpService emailOtpService;

    @Override
    @Async
    public void sendEmail(User user){
        Integer otp = emailOtpService.createOtp(user);
        String email = buildEmail(capitalize(user.getFirstName()), otp);
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Confirm your email.");
            helper.setFrom("noreply@amcrest.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email: ", e);
            throw new IllegalStateException("Failed to send email.");
        }
    }

    @Override
    public String buildEmail(String name, Integer otp) {
        return "<div style=\"font-family: Helvetica,Arial,sans-serif;min-width:1000px;overflow:auto;line-height:2\">\n" +
                "  <div style=\"margin:50px auto;width:70%;padding:20px 0\">\n" +
                "    <div style=\"border-bottom:1px solid #eee\">\n" +
                "      <a href=\"\" style=\"font-size:1.4em;color: #00466a;text-decoration:none;font-weight:600\">Amcrest Unity</a>\n" +
                "    </div>\n" +
                "    <p style=\"font-size:1.1em\">Hi "+ name +",</p>\n" +
                "    <p>Thank you for choosing Amcrest Unity. <br />" +
                "       Use the following OTP to complete your Sign Up procedures. OTP is valid for 15 minutes.</p>\n" +
                "    <h2 style=\"background: #00466a;margin: 0 auto;width: max-content;padding: 0 10px;color: #fff;border-radius: 4px;\">"+ otp +"</h2>\n" +
                "    <p style=\"font-size:0.9em;\">Regards,<br />Amcrest Unity</p>\n" +
                "    <hr style=\"border:none;border-top:1px solid #eee\" />\n" +
                "    <div style=\"float:right;padding:8px 0;color:#aaa;font-size:0.8em;line-height:1;font-weight:300\">\n" +
                "      <p>Amcrest Technologies</p>\n" +
                "      <p>16727 Park Row Drive</p>\n" +
                "      <p>Houston, TX 7708</p>\n" +
                "    </div>\n" +
                "  </div>\n" +
                "</div>";
    }
}
