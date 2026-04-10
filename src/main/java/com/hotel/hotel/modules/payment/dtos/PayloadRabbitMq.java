package com.hotel.hotel.modules.payment.dtos;

public record PayloadRabbitMq(
        Long reservationId,
        Object payload
) {
}
