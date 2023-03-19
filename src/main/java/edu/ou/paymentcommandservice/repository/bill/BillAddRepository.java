package edu.ou.paymentcommandservice.repository.bill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentcommandservice.common.constant.CodeStatus;
import edu.ou.paymentcommandservice.data.entity.BillEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class BillAddRepository extends BaseRepository<BillEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

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
    }

    /**
     * Add new bill entity
     *
     * @param billEntity input of task
     * @return id of bill
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(BillEntity billEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(billEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
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
