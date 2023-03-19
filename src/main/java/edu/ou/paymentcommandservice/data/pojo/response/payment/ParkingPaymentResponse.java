package edu.ou.paymentcommandservice.data.pojo.response.payment;

import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ParkingPaymentResponse implements IBaseResponse {
    private int parkingId;
    private int parkingTypeId;
    private int userId;
    private BigDecimal pricePerDay;
    private Timestamp joinDate;
}
