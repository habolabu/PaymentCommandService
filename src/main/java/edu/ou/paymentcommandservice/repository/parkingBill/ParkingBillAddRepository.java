package edu.ou.paymentcommandservice.repository.parkingBill;

import edu.ou.coreservice.common.constant.Message;
import edu.ou.coreservice.common.exception.BusinessException;
import edu.ou.coreservice.common.validate.ValidValidation;
import edu.ou.coreservice.repository.base.BaseRepository;
import edu.ou.paymentcommandservice.common.constant.CodeStatus;
import edu.ou.paymentcommandservice.data.entity.ParkingBillEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class ParkingBillAddRepository extends BaseRepository<ParkingBillEntity, Integer> {
    private final ValidValidation validValidation;
    private final EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    /**
     * Validate parking bill entity
     *
     * @param parkingBillEntity parking bill entity
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected void preExecute(ParkingBillEntity parkingBillEntity) {
        if (validValidation.isInValid(parkingBillEntity, ParkingBillEntity.class)) {
            throw new BusinessException(
                    CodeStatus.INVALID_INPUT,
                    Message.Error.INVALID_INPUT,
                    "parking bill"
            );
        }
        entityManager = entityManagerFactory.createEntityManager();
    }

    /**
     * Add new parking bill entity
     *
     * @param parkingBillEntity input of task
     * @return id of parking bill
     * @author Nguyen Trung Kien - OU
     */
    @Override
    protected Integer doExecute(ParkingBillEntity parkingBillEntity) {
        try {
            return (Integer)
                    entityManager
                            .unwrap(Session.class)
                            .save(parkingBillEntity);

        } catch (Exception e) {
            throw new BusinessException(
                    CodeStatus.CONFLICT,
                    Message.Error.ADD_FAIL,
                    "parking bill"
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
    protected void postExecute(ParkingBillEntity input) {
        if(Objects.nonNull(entityManager)){
            entityManager.close();
        }
    }
}
