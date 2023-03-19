package edu.ou.paymentcommandservice.data.pojo.request.bill;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.paymentcommandservice.common.constant.Status;
import lombok.Data;

@Data
public class BillUpdateRequest implements IBaseRequest {
    private int billId;
    private Status.Bill billStatus;
}
