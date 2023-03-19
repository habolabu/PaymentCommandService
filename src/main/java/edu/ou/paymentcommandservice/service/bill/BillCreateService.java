package edu.ou.paymentcommandservice.service.bill;

import edu.ou.coreservice.queue.human.external.user.UserFindDetailByIdQueueE;
import edu.ou.coreservice.queue.payment.external.payment.ParkingPaymentFindAllQueueE;
import edu.ou.coreservice.queue.payment.external.payment.RoomPaymentFindAllQueueE;
import edu.ou.coreservice.queue.payment.internal.bill.BillAddQueueI;
import edu.ou.coreservice.queue.payment.internal.parkingBill.ParkingBillAddQueueI;
import edu.ou.coreservice.queue.payment.internal.roomBill.RoomBillAddQueueI;
import edu.ou.coreservice.repository.base.IBaseRepository;
import edu.ou.paymentcommandservice.common.mapper.ParkingPaymentMapper;
import edu.ou.paymentcommandservice.common.mapper.RoomPaymentMapper;
import edu.ou.paymentcommandservice.common.mapper.UserMapper;
import edu.ou.paymentcommandservice.config.MailConfig;
import edu.ou.paymentcommandservice.config.TwilioConfig;
import edu.ou.paymentcommandservice.data.entity.BillEntity;
import edu.ou.paymentcommandservice.data.entity.ParkingBillEntity;
import edu.ou.paymentcommandservice.data.entity.PaymentHistoryTemporaryEntity;
import edu.ou.paymentcommandservice.data.entity.RoomBillEntity;
import edu.ou.paymentcommandservice.data.pojo.request.email.EmailDetailRequest;
import edu.ou.paymentcommandservice.data.pojo.response.payment.ParkingPaymentResponse;
import edu.ou.paymentcommandservice.data.pojo.response.payment.RoomPaymentResponse;
import edu.ou.paymentcommandservice.data.pojo.response.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BillCreateService {
    private final IBaseRepository<BillEntity, Integer> billAddRepository;
    private final IBaseRepository<ParkingBillEntity, Integer> parkingBillAddRepository;
    private final IBaseRepository<RoomBillEntity, Integer> roomBillAddRepository;
    private final IBaseRepository<Integer, PaymentHistoryTemporaryEntity> paymentHistoryTemporaryFindByUserIdRepository;
    private final IBaseRepository<PaymentHistoryTemporaryEntity, Integer> paymentHistoryTemporaryAddRepository;
    private final IBaseRepository<PaymentHistoryTemporaryEntity, Integer> paymentHistoryTemporaryUpdateRepository;
    private final RabbitTemplate rabbitTemplate;
    private final MailConfig mailConfig;
    private final TwilioConfig twilioConfig;

    /**
     * Auto create bill every month at 1st
     * @author Nguyen Trung Kien - OU
     */
    @Scheduled(cron = "0 0 0 1 * ?")
    public void execute() {
        // call building service to get room payment detail
        final Object roomPayments = rabbitTemplate.convertSendAndReceive(
                RoomPaymentFindAllQueueE.EXCHANGE,
                RoomPaymentFindAllQueueE.ROUTING_KEY,
                0
        );

        // call human service to get room payment detail
        final Object parkingPayments = rabbitTemplate.convertSendAndReceive(
                ParkingPaymentFindAllQueueE.EXCHANGE,
                ParkingPaymentFindAllQueueE.ROUTING_KEY,
                0
        );

        // map data
        final List<Map<String, Object>> roomPaymentMaps = (List<Map<String, Object>>) roomPayments;
        final List<Map<String, Object>> parkingPaymentMaps = (List<Map<String, Object>>) parkingPayments;

        final List<RoomPaymentResponse> roomPaymentResponses = new ArrayList<>();
        final List<ParkingPaymentResponse> parkingPaymentResponses = new ArrayList<>();

        assert roomPaymentMaps != null;
        roomPaymentMaps.forEach(roomPaymentMap -> roomPaymentResponses.add(RoomPaymentMapper.INSTANCE
                .fromMap(roomPaymentMap)));

        assert parkingPaymentMaps != null;
        parkingPaymentMaps.forEach(parkingPaymentMap -> parkingPaymentResponses.add(ParkingPaymentMapper.INSTANCE
                .fromMap(parkingPaymentMap)));

        final Map<Integer, List<RoomPaymentResponse>> roomPaymentResponseMap = roomPaymentResponses.stream()
                .collect(Collectors.groupingBy(RoomPaymentResponse::getOwnerId));

        final Map<Integer, List<ParkingPaymentResponse>> parkingPaymentResponseMap = parkingPaymentResponses.stream()
                .collect(Collectors.groupingBy(ParkingPaymentResponse::getUserId));


        // create bill
        new HashSet<Integer>() {
            {
                addAll(roomPaymentResponseMap.keySet());
                addAll(parkingPaymentResponseMap.keySet());
            }
        }.forEach(userId -> {
            final PaymentHistoryTemporaryEntity paymentHistoryTemporary =
                    paymentHistoryTemporaryFindByUserIdRepository.execute(userId);
            final List<RoomPaymentResponse> roomPaymentDetails = roomPaymentResponseMap.get(userId);
            final List<ParkingPaymentResponse> parkingPaymentDetails = parkingPaymentResponseMap.get(userId);

            final BillEntity bill = new BillEntity().setUserId(userId);
            final List<RoomBillEntity> roomBills = new ArrayList<>();
            final List<ParkingBillEntity> parkingBills = new ArrayList<>();

            if (Objects.nonNull(paymentHistoryTemporary)) {
                final int dayAmount = 1 + (int) ChronoUnit.DAYS.between(
                        paymentHistoryTemporary.getNearestPaidDate().toInstant(),
                        new Timestamp(System.currentTimeMillis()).toInstant()

                );
                // room payment list
                if (Objects.nonNull(roomPaymentDetails)) {
                    roomPaymentDetails.forEach(roomPaymentResponse -> {
                                final RoomBillEntity roomBill = new RoomBillEntity()
                                        .setDayAmount(dayAmount)
                                        .setTotal(
                                                roomPaymentResponse
                                                        .getPricePerDay()
                                                        .multiply(new BigDecimal(dayAmount))
                                        )
                                        .setRoomId(roomPaymentResponse.getRoomId());


                                bill.setTotal(bill.getTotal().add(roomBill.getTotal()));
                                roomBills.add(roomBill);
                            }
                    );

                }

                // parking payment list
                if (Objects.nonNull(parkingPaymentDetails)) {
                    parkingPaymentDetails.forEach(parkingPaymentResponse -> {
                                final ParkingBillEntity parkingBill = new ParkingBillEntity()
                                        .setDayAmount(dayAmount)
                                        .setTotal(
                                                parkingPaymentResponse
                                                        .getPricePerDay()
                                                        .multiply(new BigDecimal(dayAmount))
                                        )
                                        .setParkingId(parkingPaymentResponse.getParkingId())
                                        .setParkingTypeId(parkingPaymentResponse.getParkingTypeId());

                                bill.setTotal(bill.getTotal().add(parkingBill.getTotal()));
                                parkingBills.add(parkingBill);

                            }
                    );
                }

                paymentHistoryTemporaryUpdateRepository.execute(
                        paymentHistoryTemporary.setNearestPaidDate(new Timestamp(System.currentTimeMillis())));
            } else {
                // room payment list
                if (Objects.nonNull(roomPaymentDetails)) {
                    roomPaymentDetails.forEach(roomPaymentResponse -> {
                                final int dayAmount = 1 + (int) ChronoUnit.DAYS.between(
                                        roomPaymentResponse.getJoinDate().toInstant(),
                                        new Timestamp(System.currentTimeMillis()).toInstant()
                                );

                                final RoomBillEntity roomBill = new RoomBillEntity()
                                        .setDayAmount(dayAmount)
                                        .setTotal(
                                                roomPaymentResponse
                                                        .getPricePerDay()
                                                        .multiply(new BigDecimal(dayAmount)))
                                        .setRoomId(roomPaymentResponse.getRoomId());

                                bill.setTotal(bill.getTotal().add(roomBill.getTotal()));
                                roomBills.add(roomBill);
                            }
                    );
                }

                // parking payment list
                if (Objects.nonNull(parkingPaymentDetails)) {
                    parkingPaymentDetails.forEach(parkingPaymentResponse -> {
                                final int dayAmount = 1 + (int) ChronoUnit.DAYS.between(
                                        parkingPaymentResponse.getJoinDate().toInstant(),
                                        new Timestamp(System.currentTimeMillis()).toInstant()
                                );

                                final ParkingBillEntity parkingBill = new ParkingBillEntity()
                                        .setDayAmount(dayAmount)
                                        .setTotal(
                                                parkingPaymentResponse
                                                        .getPricePerDay()
                                                        .multiply(new BigDecimal(dayAmount))
                                        )
                                        .setParkingId(parkingPaymentResponse.getParkingId())
                                        .setParkingTypeId(parkingPaymentResponse.getParkingTypeId());

                                bill.setTotal(bill.getTotal().add(parkingBill.getTotal()));
                                parkingBills.add(parkingBill);
                            }
                    );
                }

                paymentHistoryTemporaryAddRepository.execute(paymentHistoryTemporary.setUserId(userId));
            }

            final int billId = billAddRepository.execute(bill);
            bill.setId(billId);
            rabbitTemplate.convertSendAndReceive(
                    BillAddQueueI.EXCHANGE,
                    BillAddQueueI.ROUTING_KEY,
                    bill
            );

            roomBills.forEach(roomBillEntity -> {
                roomBillEntity.setBillId(billId);
                final int roomBillId = roomBillAddRepository.execute(roomBillEntity);
                roomBillEntity.setId(roomBillId);

                rabbitTemplate.convertSendAndReceive(
                        RoomBillAddQueueI.EXCHANGE,
                        RoomBillAddQueueI.ROUTING_KEY,
                        roomBillEntity
                );
            });

            parkingBills.forEach(parkingBillEntity -> {
                parkingBillEntity.setBillId(billId);
                final int parkingBillId = parkingBillAddRepository.execute(parkingBillEntity);
                parkingBillEntity.setId(parkingBillId);

                rabbitTemplate.convertSendAndReceive(
                        ParkingBillAddQueueI.EXCHANGE,
                        ParkingBillAddQueueI.ROUTING_KEY,
                        parkingBillEntity
                );
            });

            final Object userInfo = rabbitTemplate.convertSendAndReceive(
                    UserFindDetailByIdQueueE.EXCHANGE,
                    UserFindDetailByIdQueueE.ROUTING_KEY,
                    userId
            );
            final UserResponse userDetail = UserMapper.INSTANCE.fromMap((Map<String, Object>) userInfo);

            mailConfig.sendMail(
                    new EmailDetailRequest()
                            .setSubject("Hóa đơn cư dân từ Habolabu")
                            .setRecipient(userDetail.getEmail())
                            .setMessageBody(
                                    String.format("Hóa đơn tháng này của khách hàng %s %s có mã số %d đã được tạo. " +
                                                    "Xin vui lòng xem chi tiết trên trang web của Habolabu",
                                            userDetail.getLastName(),
                                            userDetail.getFirstName(),
                                            userId)
                            )
            );

//            twilioConfig.sendMessage(
//                    "+84982482975",
//                    String.format("Hóa đơn tháng này của khách hàng %s %s có mã số %d đã được tạo. " +
//                                    "Xin vui lòng xem chi tiết trên trang web của Habolabu",
//                            userDetail.getLastName(),
//                            userDetail.getFirstName(),
//                            userId)
//            );
        });

    }

}
