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
import javax.persistence.NoResultException;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class BillFindByIdRepository extends BaseRepository<Integer, BillEntity> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate input
     *
     * @param billId bill identity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(Integer billId) {
        if (validValidation.isInValid(billId)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "bill identity"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Find bill by id
     *
     * @param billId bill id
     * @return bill
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected BillEntity doExecute(Integer billId) {
        final String hqlQuery = "FROM BillEntity B WHERE B.id = :billId";

        try {
            return (BillEntity)
                    entityManager
                            .unwrap(Session.class)
                            .createQuery(hqlQuery)
                            .setParameter(
                                    "billId",
                                    billId
                            )
                            .getSingleResult();

        } catch (NoResultException e) {
            throw new BusinessException(
                    CodeStatus.NOT_FOUND,
                    Message.Error.NOT_FOUND,
                    "bill",
                    "bill identity",
                    billId
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
    protected void postExecute(Integer input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
