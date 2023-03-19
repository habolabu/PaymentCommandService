package edu.ou.paymentcommandservice.controller.bill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.paymentcommandservice.common.constant.CodeStatus;
import edu.ou.paymentcommandservice.common.constant.EndPoint;
import edu.ou.paymentcommandservice.common.constant.Status;
import edu.ou.paymentcommandservice.data.pojo.request.bill.BillUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.Bill.BASE)
public class BillCompletePayController {
    private final IBaseService<IBaseRequest, IBaseResponse> billUpdateService;

    /**
     * pay bill
     *
     * @return momo response
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.PERMIT_ALL)
    @GetMapping(
            value = EndPoint.Bill.PAY_COMPLETE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> completeBill(
            @PathVariable int billId,
            @RequestParam int resultCode
    ) {
        final IBaseResponse failResponse = new SuccessResponse<>(
                new SuccessPojo<>(
                        billId,
                        CodeStatus.CONFLICT,
                        Message.Error.TITLE
                )
        );

        final IBaseResponse successResponse = billUpdateService.execute(
                new BillUpdateRequest()
                        .setBillId(billId)
                        .setBillStatus(Status.Bill.PAID)
        );

        return new ResponseEntity<>(
                resultCode == 0 ? successResponse : failResponse,
                HttpStatus.OK
        );
    }

}
