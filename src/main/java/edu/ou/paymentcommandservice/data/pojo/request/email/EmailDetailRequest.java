package edu.ou.paymentcommandservice.data.pojo.request.email;

import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import lombok.Data;

@Data
public class EmailDetailRequest implements IBaseRequest {
    private String recipient;
    private String messageBody;
    private String subject;
}
