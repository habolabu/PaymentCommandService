package edu.ou.paymentcommandservice.data.pojo.response.payment;

import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class RoomPaymentResponse implements IBaseResponse {
    private int roomId;
    private int ownerId;
    private BigDecimal pricePerDay;
    private Timestamp joinDate;
}
