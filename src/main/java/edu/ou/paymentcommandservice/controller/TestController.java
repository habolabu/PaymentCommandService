package edu.ou.paymentcommandservice.controller;

import edu.ou.coreservice.common.constant.SecurityPermission;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.paymentcommandservice.service.bill.BillCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: will be remove
@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class TestController {
    private final BillCreateService billCreateService;


    @PreAuthorize(SecurityPermission.AUTHENTICATED)
    @GetMapping()
    public ResponseEntity<IBaseResponse> test() {
        billCreateService.execute();
        return new ResponseEntity<>(
                null,
                HttpStatus.OK
        );
    }
}
