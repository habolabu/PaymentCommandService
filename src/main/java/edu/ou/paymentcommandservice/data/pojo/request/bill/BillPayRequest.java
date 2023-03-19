package edu.ou.paymentcommandservice.data.pojo.request.bill;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class BillPayRequest implements IBaseRequest {
    @Min(
            value = 1,
            message = "Value must greater than or equals to 1"
    )
    private int billId;

    @NotBlank
    private String returnUrl;
}
