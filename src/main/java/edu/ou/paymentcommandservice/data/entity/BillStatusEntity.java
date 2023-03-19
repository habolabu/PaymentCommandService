package edu.ou.paymentcommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(
        name = "BillStatus",
        schema = "PaymentCommandService"
)
public class BillStatusEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @NotBlank
    @Basic
    @Column(name = "name")
    private String name;

    @NotBlank
    @Basic
    @Column(name = "slug")
    private String slug;

    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;
}
