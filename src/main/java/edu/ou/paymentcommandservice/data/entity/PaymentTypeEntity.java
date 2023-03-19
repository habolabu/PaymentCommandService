package edu.ou.paymentcommandservice.data.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(
        name = "PaymentType",
        schema = "PaymentCommandService"
)
public class PaymentTypeEntity implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "name")
    @NotBlank
    private String name;

    @Basic
    @Column(name = "slug")
    @NotBlank
    private String slug;

    @Basic
    @Column(name = "isDeleted")
    private Timestamp isDeleted;

}
