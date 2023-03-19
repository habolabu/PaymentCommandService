package edu.ou.paymentcommandservice.repository.bill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentcommandservice.common.constant.CodeStatus;
import edu.ou.paymentcommandservice.data.entity.BillEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class BillUpdateRepository extends BaseRepository<BillEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate bill entity
     *
     * @param billEntity bill entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(BillEntity billEntity) {
        if (validValidation.isInValid(billEntity, BillEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "bill"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update a bill
     *
     * @param bill bill
     * @return id of bill
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(BillEntity bill) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            BillEntity.class,
                            bill.getId()
                    )
                    .setBillStatusId(bill.getBillStatusId())
                    .setUserId(bill.getUserId())
                    .setPaymentTypeId(bill.getPaymentTypeId())
                    .setPaidDate(bill.getPaidDate())
                    .setTotal(bill.getTotal());
            entityTransaction.commit();

            return bill.getId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "bill"
            );

        }
    }

    /**
     * Close connection
     *
     * @param input input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(BillEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
