package model;

import java.math.BigDecimal;
import java.time.LocalTime;

import integration.ItemDTO;

public record SaleDTO(int id, LocalTime dateTime, ItemDTO[] boughtItems, BigDecimal totalPrice, BigDecimal totalVat,
        BigDecimal amountPaid, BigDecimal change) {}
