package edu.ou.paymentcommandservice;

import edu.ou.coreservice.annotation.BaseCommandAnnotation;
import org.springframework.boot.SpringApplication;

@BaseCommandAnnotation
public class PaymentCommandServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentCommandServiceApplication.class, args);
    }

}
