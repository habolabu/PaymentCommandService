package edu.ou.paymentcommandservice.common.util;

public class OTPUtils {
    /**
     * Generate OTP
     *
     * @return OTP value
     */
    public String generate() {
        return String.valueOf((int) (Math.random() * 900000) + 100000);
    }
}
