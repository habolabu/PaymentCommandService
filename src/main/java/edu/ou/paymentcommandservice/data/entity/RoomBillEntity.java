package edu.ou.paymentcommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Entity
@Table(
        name = "RoomBill",
        schema = "PaymentCommandService"
)
public class RoomBillEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "billId")
    @Min(
            value = 1,
            message = "Value must greater or equals 1"
    )
    private int billId;

    @Basic
    @Column(name = "roomId")
    @Min(
            value = 1,
            message = "Value must greater or equals 1"
    )
    private int roomId;

    @Basic
    @Column(name = "dayAmount")
    @Min(
            value = 1,
            message = "Value must greater or equals 1"
    )
    private int dayAmount;

    @Basic
    @Column(name = "total")
    @NotNull
    @Min(
            value = 1,
            message = "Value must greater or equals 1"
    )
    private BigDecimal total;
}
