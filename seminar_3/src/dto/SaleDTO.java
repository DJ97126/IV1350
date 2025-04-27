package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleDTO(LocalDateTime saleDateTime, ItemDTO[] boughtItems, BigDecimal totalPrice, BigDecimal totalVAT, BigDecimal amountPaid ,BigDecimal change) {}
