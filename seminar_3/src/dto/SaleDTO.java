package dto;

import java.math.BigDecimal;

public record SaleDTO(ItemDTO[] boughtItems, BigDecimal change) {}
