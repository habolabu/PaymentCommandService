package edu.ou.paymentcommandservice.repository.roomBill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentcommandservice.common.constant.CodeStatus;
import edu.ou.paymentcommandservice.data.entity.RoomBillEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class RoomBillAddRepository extends BaseRepository<RoomBillEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate room bill entity
     *
     * @param roomBillEntity room bill entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(RoomBillEntity roomBillEntity) {
        if (validValidation.isInValid(roomBillEntity, RoomBillEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "room"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new room bill entity
     *
     * @param roomBillEntity input of task
     * @return id of room bill
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(RoomBillEntity roomBillEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(roomBillEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "room bill"
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
    protected void postExecute(RoomBillEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
