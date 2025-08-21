package com.example.validatorservice.event;

import java.math.BigDecimal;

public record ItemDto(
        String name,
        BigDecimal price,
        BigDecimal quantity,
        BigDecimal sum,
        Integer invoiceType,
        BigDecimal invoiceSum,
        Integer productType,
        String paymentType
) {
}