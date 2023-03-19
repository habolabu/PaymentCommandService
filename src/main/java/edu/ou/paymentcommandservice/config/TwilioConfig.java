package edu.ou.paymentcommandservice.config;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
@RequiredArgsConstructor
public class TwilioConfig {
    @Value("${spring.twilio.accountSID}")
    private String accountSID;
    @Value("${spring.twilio.authToken}")
    private String authToken;
    @Value("${spring.twilio.sender}")
    private String sender;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * Send sms message
     *
     * @param phoneNumber phone number
     * @param content     message content
     * @author Nguyen Trung Kien - OU
     */
    public void sendMessage(String phoneNumber, String content) {
        threadPoolTaskExecutor.execute(() -> {
                    Twilio.init(
                            Objects.requireNonNull(accountSID),
                            Objects.requireNonNull(authToken)
                    );

                    Message
                            .creator(
                                    new PhoneNumber(phoneNumber),
                                    new PhoneNumber(sender),
                                    content
                            )
                            .create();
                }
        );

    }
}
