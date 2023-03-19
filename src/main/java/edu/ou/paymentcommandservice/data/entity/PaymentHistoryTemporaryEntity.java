package edu.ou.paymentcommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(
        name = "PaymentHistoryTemporary",
        schema = "PaymentCommandService"
)
public class PaymentHistoryTemporaryEntity implements Serializable {
    @Id
    @Column(name = "userId")
    private int userId;

    @Basic
    @Column(name = "nearestPaidDate")
    private Timestamp nearestPaidDate = new Timestamp(System.currentTimeMillis());
}
