package edu.ou.paymentcommandservice.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Status {

    public enum Bill {
        CREATED,
        NOT_PAID,
        REJECTED,
        PAID
    }
}
