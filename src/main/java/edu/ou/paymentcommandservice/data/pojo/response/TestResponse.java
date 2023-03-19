package edu.ou.paymentcommandservice.data.pojo.response;

import edu.ou.coreservice.data.pojo.response.base.IBaseResponse;
import edu.ou.paymentcommandservice.data.entity.BillEntity;
import edu.ou.paymentcommandservice.data.entity.ParkingBillEntity;
import edu.ou.paymentcommandservice.data.entity.RoomBillEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// TODO remove later

@Data
public class TestResponse implements IBaseResponse {
    private List<BillEntity> bill = new ArrayList<>();
    private List<List<RoomBillEntity>> roomBillEntities = new ArrayList<>();
    private List<List<ParkingBillEntity>> parkingBillEntities = new ArrayList<>();
}
