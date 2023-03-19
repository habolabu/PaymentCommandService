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
public class PaymentHistoryTemporaryFindByUserIdRepository
        extends BaseRepository<Integer, PaymentHistoryTemporaryEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate user id
     *
     * @param userId id of user
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer userId) {
        if (validValidation.isInValid(userId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "user identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Find payment history temporary by user id
     *
     * @param userId id of user
     * @return payment history temporary
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected PaymentHistoryTemporaryEntity doExecute(Integer userId) {
        final String hqlQuery = "FROM PaymentHistoryTemporaryEntity P WHERE P.userId = :userId ";

        try {
            return (PaymentHistoryTemporaryEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "userId",
                                    userId
                            )
                            .getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Close connection
     *
     * @param apartmentId input of task
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void postExecute(Integer apartmentId) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
