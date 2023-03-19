package edu.ou.paymentcommandservice.common.mapper;

import edu.ou.paymentcommandservice.data.pojo.response.user.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Map HashMap<String, String> object to RoomPayment object
     *
     * @param map represents for RoomPaymentMap object
     * @return RoomPayment object
     * @author Nguyen Trung Kien - OU
     */
    @Mapping(target = "firstName", source = "firstName", qualifiedByName = "objectToString")
    @Mapping(target = "lastName", source = "lastName", qualifiedByName = "objectToString")
    @Mapping(target = "phoneNumber", source = "phoneNumber", qualifiedByName = "objectToString")
    @Mapping(target = "email", source = "email", qualifiedByName = "objectToString")
    UserResponse fromMap(Map<String, Object> map);

    /**
     * Convert object to String
     *
     * @param object object will be converted
     * @return String object
     * @author Nguyen Trung Kien - OU
     */
    @Named("objectToString")
    static String objectToString(Object object) {
        return (String) object;
    }
}
