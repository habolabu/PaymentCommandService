package edu.ou.paymentcommandservice.service.bill;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.paymentcommandservice.common.constant.CodeStatus;
import edu.ou.paymentcommandservice.config.MomoConfig;
import edu.ou.paymentcommandservice.data.entity.BillEntity;
import edu.ou.paymentcommandservice.data.pojo.request.bill.BillPayRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;

@Service
@Transactional
@RequiredArgsConstructor
public class BillPayService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Integer, BillEntity> billFindByIdRepository;
    private final MomoConfig momoConfig;

    @Value("${spring.momo.notifyURL}")
    private String notifyURL;

    @Override
    protected void preExecute(IBaseRequest input) {
        // do nothing
    }

    /**
     * pay bill
     *
     * @param request request from client
     * @return response to client
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final BillPayRequest billPaidRequest = (BillPayRequest) request;
        final BillEntity existBill = billFindByIdRepository.execute(billPaidRequest.getBillId());

        if (existBill.getBillStatusId() != 2) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "bill status"
            );
        }

        try {
            return new SuccessResponse<>(
                    new SuccessPojo<>(
                            momoConfig.createOrder(
                                    existBill.getTotal(),
                                    String.format(
                                            "Mã người dùng: %d, mã đơn hàng: %d",
                                            existBill.getUserId(),
                                            existBill.getId()
                                    ),
                                    String.format(notifyURL, existBill.getId()),
                                    billPaidRequest.getReturnUrl()

                            ),
                            CodeStatus.SUCCESS,
                            Message.Success.SUCCESSFUL
                    )
            );
        } catch (InvalidKeyException
                 | NoSuchAlgorithmException
                 | JsonProcessingException
                 | ExecutionException
                 | InterruptedException e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.TITLE
            );
        }
    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }
}
