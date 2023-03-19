package edu.ou.paymentcommandservice.controller.bill;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.paymentcommandservice.common.constant.EndPoint;
import edu.ou.paymentcommandservice.data.pojo.request.bill.BillPayRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.Bill.BASE)
public class BillPayController {
    private final IBaseService<IBaseRequest, IBaseResponse> billPayService;

    /**
     * pay bill
     *
     * @return momo response
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @PostMapping(
            value = EndPoint.Bill.PAY,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> payBill(
            @RequestBody
            BillPayRequest billPayRequest
    ) {
        return new ResponseEntity<>(
                billPayService.execute(billPayRequest),
                HttpStatus.OK
        );
    }
}
