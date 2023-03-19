package edu.ou.paymentcommandservice.config;

import edu.ou.paymentcommandservice.data.pojo.request.email.EmailDetailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailConfig {

    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender javaMailSender;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * Send mail
     *
     * @param emailDetail mail detail
     * @return status of result
     * @author Nguyen Trung Kien - OU
     */
    public boolean sendMail(EmailDetailRequest emailDetail) {
        try {
            threadPoolTaskExecutor.execute(() -> {
                final SimpleMailMessage mailMessage = new SimpleMailMessage();

                mailMessage.setFrom(sender);
                mailMessage.setTo(emailDetail.getRecipient());
                mailMessage.setText(emailDetail.getMessageBody());
                mailMessage.setSubject(emailDetail.getSubject());

                javaMailSender.send(mailMessage);

            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
