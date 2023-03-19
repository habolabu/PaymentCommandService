package edu.ou.paymentcommandservice.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndPoint {
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static final class Bill {
        public static final String BASE = "/bill";
        public static final String APPROVE = "/approve/{billId}";
        public static final String REJECT = "/reject/{billId}";
        public static final String PAY = "/pay";
        public static final String PAY_COMPLETE = "/pay/complete/{billId}";
    }

}
