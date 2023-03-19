package edu.ou.paymentcommandservice.common.mapper;

import edu.ou.paymentcommandservice.data.pojo.response.payment.ParkingPaymentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Map;

@Mapper
public interface ParkingPaymentMapper {
    ParkingPaymentMapper INSTANCE = Mappers.getMapper(ParkingPaymentMapper.class);

    /**
     * Map HashMap<String, String> object to ParkingPayment object
     *
     * @param map represents for ParkingPaymentMap object
     * @return ParkingPayment object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "parkingId", source = "parkingId", qualifiedByName = "objectToInt")
    @Mapping(target = "parkingTypeId", source = "parkingTypeId", qualifiedByName = "objectToInt")
    @Mapping(target = "userId", source = "userId", qualifiedByName = "objectToInt")
    @Mapping(target = "pricePerDay", source = "pricePerDay", qualifiedByName = "objectToBigDecimal")
    @Mapping(target = "joinDate", source = "joinDate", qualifiedByName = "objectToTimeStamp")
    ParkingPaymentResponse fromMap(Map<String, Object> map);

    /**
     * Covert object to int
     *
     * @param object object will be converted
     * @return int value
     * @author Nguyen Trung Kien - OU
     */
    @Named("objectToInt")
    static int objectToInt(Object object) {
        return (int) object;
    }

    /**
     * Convert object to BigDecimal
     *
     * @param object object will be convert
     * @return BigDecimal value
     * @author Nguyen Trung Kien - OU
     */
    @Named("objectToBigDecimal")
    static BigDecimal objectToBigDecimal(Object object) {
        return BigDecimal.valueOf((int) object);
    }

    /**
     * Convert object to TimeStamp
     *
     * @param object object will be convert
     * @return TimeStamp object
     * @author Nguyen Trung Kien - OU
     */
    @Named("objectToTimeStamp")
    static Timestamp objectToTimeStamp(Object object) {
        return Timestamp.from(Instant.parse((String) object));
    }

}
