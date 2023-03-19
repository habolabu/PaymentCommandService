package edu.ou.paymentcommandservice.data.pojo.response.user;

import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import lombok.Data;

@Data
public class UserResponse implements IBaseResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
}
