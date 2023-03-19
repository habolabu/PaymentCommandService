package edu.ou.paymentcommandservice.repository.paymentHistoryTemporary;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentcommandservice.common.constant.CodeStatus;
import edu.ou.paymentcommandservice.data.entity.PaymentHistoryTemporaryEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class PaymentHistoryTemporaryAddRepository extends BaseRepository<PaymentHistoryTemporaryEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;


    /**
     * Validate payment history entity
     *
     * @param paymentHistoryTemporaryEntity payment history entity
     *                                      @author Nguyen Trung Kien - OU
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
    }

    /**
     * Add new payment history temporary entity
     *
     * @param paymentHistoryTemporaryEntity input of task
     * @return id of payment history temporary
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(PaymentHistoryTemporaryEntity paymentHistoryTemporaryEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(paymentHistoryTemporaryEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "payment history temporary"
            );
        }
    }

    /**
     * Close connection
     *
     * @param input input of task
     *              @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(PaymentHistoryTemporaryEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
