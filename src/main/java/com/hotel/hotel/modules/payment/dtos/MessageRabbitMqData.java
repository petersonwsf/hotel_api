package com.hotel.hotel.modules.payment.dtos;

public record MessageRabbitMqData(
        String pattern,
        PayloadRabbitMq data
) {}
