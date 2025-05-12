package dto;

import java.math.BigDecimal;

public record SaleInfoDTO(ItemDTO currentItem, BigDecimal totalPrice, BigDecimal totalVat) {}
