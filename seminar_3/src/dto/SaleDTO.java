package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SaleDTO(LocalDateTime saleDateTime, ItemDTO[] boughtItems, BigDecimal totalPrice, BigDecimal totalVat, BigDecimal amountPaid ,BigDecimal change) {}
