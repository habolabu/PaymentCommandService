package edu.ou.paymentcommandservice.service.bill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.data.pojo.request.base.IBaseRequest;
import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.coreservice.data.pojo.response.impl.SuccessPojo;
import edu.ou.coreservice.data.pojo.response.impl.SuccessResponse;
import edu.ou.coreservice.queue.payment.internal.bill.BillUpdateQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.coreservice.service.base.BaseService;
import edu.ou.paymentcommandservice.common.constant.CodeStatus;
import edu.ou.paymentcommandservice.common.constant.Status;
import edu.ou.paymentcommandservice.data.entity.BillEntity;
import edu.ou.paymentcommandservice.data.pojo.request.bill.BillUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
@Transactional
@RequiredArgsConstructor
public class BillUpdateService extends BaseService<IBaseRequest, IBaseResponse> {
    private final IBaseRepository<Integer, BillEntity> billFindByIdRepository;
    private final IBaseRepository<BillEntity, Integer> billUpdateRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    protected void preExecute(IBaseRequest input) {
        // do nothing
    }

    /**
     * Update bill
     *
     * @param request request of task
     * @return id of bill
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected IBaseResponse doExecute(IBaseRequest request) {
        final BillUpdateRequest billUpdateRequest = (BillUpdateRequest) request;
        final BillEntity existBill = billFindByIdRepository.execute(billUpdateRequest.getBillId());

        if (Status.Bill.CREATED != billUpdateRequest.getBillStatus()) {
            if (billUpdateRequest.getBillStatus().equals(Status.Bill.PAID)) {
                existBill.setPaidDate(new Timestamp(System.currentTimeMillis()));
            }

            existBill.setBillStatusId(billUpdateRequest.getBillStatus().ordinal() + 1);

            billUpdateRepository.execute(existBill);

            rabbitTemplate.convertSendAndReceive(
                    BillUpdateQueueI.EXCHANGE,
                    BillUpdateQueueI.ROUTING_KEY,
                    existBill
            );
        }

        return new SuccessResponse<>(
                new SuccessPojo<>(
                        billUpdateRequest.getBillId(),
                        CodeStatus.SUCCESS,
                        Message.Success.SUCCESSFUL
                )
        );
    }

    @Override
    protected void postExecute(IBaseRequest input) {
        // do nothing
    }
}
