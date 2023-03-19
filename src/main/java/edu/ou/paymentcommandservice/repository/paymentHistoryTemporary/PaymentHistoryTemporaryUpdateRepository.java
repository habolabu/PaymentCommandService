package edu.ou.paymentcommandservice.repository.paymentHistoryTemporary;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentcommandservice.common.constant.CodeStatus;
import edu.ou.paymentcommandservice.data.entity.PaymentHistoryTemporaryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryTemporaryUpdateRepository
        extends BaseRepository<PaymentHistoryTemporaryEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private EntityTransaction entityTransaction;

    /**
     * Validate paymentHistoryTemporaryEntity
     *
     * @param paymentHistoryTemporaryEntity paymentHistoryTemporaryEntity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(PaymentHistoryTemporaryEntity paymentHistoryTemporaryEntity) {
        if (validValidation.isInValid(paymentHistoryTemporaryEntity, PaymentHistoryTemporaryEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "payment history temporary"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
        entityTransaction = entityManager.getTransaction();
    }

    /**
     * Update exist payment history temporary
     *
     * @param paymentHistoryTemporaryEntity paymentHistoryTemporaryEntity information
     * @return id of updated paymentHistoryTemporaryEntity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(PaymentHistoryTemporaryEntity paymentHistoryTemporaryEntity) {
        try {
            entityTransaction.begin();
            entityManager
                    .find(
                            PaymentHistoryTemporaryEntity.class,
                            paymentHistoryTemporaryEntity.getUserId()
                    )
                    .setUserId(paymentHistoryTemporaryEntity.getUserId())
                    .setNearestPaidDate(paymentHistoryTemporaryEntity.getNearestPaidDate());
            entityTransaction.commit();

            return paymentHistoryTemporaryEntity.getUserId();

        } catch (Exception e) {
            entityTransaction.rollback();

            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.UPDATE_FAIL,
                    "payment history temporary"
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
    protected void postExecute(PaymentHistoryTemporaryEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}

