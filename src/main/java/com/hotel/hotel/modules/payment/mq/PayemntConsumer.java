package com.hotel.hotel.modules.payment.mq;

import com.hotel.hotel.modules.payment.dtos.MessageRabbitMqData;
import com.hotel.hotel.modules.reservation.service.ReservationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PayemntConsumer {

    @Autowired
    private ReservationService reservationService;

    @RabbitListener(queues = "payment_queue")
    public void handleSuccessfullyPayment(MessageRabbitMqData data) {
        String action = data.pattern();
        Long reservationId = data.data().reservationId();
        if ("reservation_confirmed".equals(action)) {
            log.info("Received a request by rabbitmq to confirm reservation with ID: {}", reservationId);
            reservationService.confirm(reservationId);
        } else if ("reservation_canceled".equals(action)) {
            log.info("Received a request by rabbitmq to cancel reservation with ID: {}", reservationId);
            reservationService.cancel(reservationId);
        }
    }
}
