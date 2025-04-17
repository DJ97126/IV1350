package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import integration.ItemDTO;

public record ReceiptDTO(LocalDateTime saleDateTime, ItemDTO[] boughtItems, BigDecimal totalPrice, BigDecimal totalVat,
        BigDecimal amountPaid, BigDecimal change) {}
