package edu.ou.paymentcommandservice.common.mapper;

import edu.ou.paymentcommandservice.data.pojo.response.payment.RoomPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

@Mapper
public interface RoomPaymentMapper {
    RoomPaymentMapper INSTANCE = Mappers.getMapper(RoomPaymentMapper.class);

    /**
     * Map HashMap<String, String> object to RoomPayment object
     *
     * @param map represents for RoomPaymentMap object
     * @return RoomPayment object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "roomId", source = "roomId", qualifiedByName = "objectToInt")
    @Mapping(target = "ownerId", source = "ownerId", qualifiedByName = "objectToInt")
    @Mapping(target = "pricePerDay", source = "pricePerDay", qualifiedByName = "objectToBigDecimal")
    @Mapping(target = "joinDate", source = "joinDate", qualifiedByName = "objectToTimeStamp")
    RoomPaymentResponse fromMap(Map<String, Object> map);

    /**
     * Convert object to int
     *
     * @param object object will be converted
     * @return int value
     */
    @Named("objectToInt")
    static int objectToInt(Object object) {
        return (int) object;
    }

    /**
     * Convert object to BigDecimal
     *
     * @param object object will be converted
     * @return BigDecimal object
     * @author Nguyen Trung Kien - OU
     */
    @Named("objectToBigDecimal")
    static BigDecimal objectToBigDecimal(Object object) {
        return BigDecimal.valueOf((int) object);
    }

    /**
     * Convert object to TimeStamp
     *
     * @param object object will be converted
     * @return TimeStamp object
     * @author Nguyen Trung Kien - OU
     */
    @Named("objectToTimeStamp")
    static Timestamp objectToTimeStamp(Object object) {
        return Timestamp.from(Instant.parse((String) object));
    }
}
