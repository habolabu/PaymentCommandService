package edu.ou.paymentcommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(
        name = "Bill",
        schema = "PaymentCommandService"
)
public class BillEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "total")
    @NotNull
    private BigDecimal total = new BigDecimal(0);

    @Basic
    @Column(name = "paidDate")
    private Timestamp paidDate;

    @NotNull
    @Basic
    @Column(name = "createdAt")
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    @Basic
    @Column(name = "userId")
    @Min(
            value = 1,
            message = "Value must greater or equals 1"
    )
    private int userId;

    @Basic
    @Column(name = "paymentTypeId")
    @Min(
            value = 1,
            message = "Value must greater or equals 1"
    )
    private int paymentTypeId = 1;

    @Basic
    @Column(name = "billStatusId")
    @Min(
            value = 1,
            message = "Value must greater or equals 1"
    )
    private int billStatusId = 1;
}
