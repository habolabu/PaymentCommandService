package edu.ou.paymentcommandservice.controller.bill;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.service.base.IBaseService;
import edu.ou.paymentcommandservice.common.constant.EndPoint;
import edu.ou.paymentcommandservice.common.constant.Status;
import edu.ou.paymentcommandservice.data.pojo.request.bill.BillUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(EndPoint.Bill.BASE)
public class BillRejectController {
    private final IBaseService<IBaseRequest, IBaseResponse> billUpdateService;

    /**
     * Reject bill
     *
     * @return id of bill
     * @author Nguyen Trung Kien - OU
     */
    @PreAuthorize(SecurityPermission.REJECT_BILL)
    @PutMapping(
            value = EndPoint.Bill.REJECT,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<IBaseResponse> rejectBill(
            @PathVariable int billId
    ) {
        return new ResponseEntity<>(
                billUpdateService.execute(
                        new BillUpdateRequest()
                                .setBillId(billId)
                                .setBillStatus(Status.Bill.REJECTED)
                ),
                HttpStatus.OK
        );
    }
}
