package dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record SaleDTO(int saleId, LocalDateTime saleDateTime, List<ItemDTO> boughtItems, BigDecimal totalPrice,
		BigDecimal totalVat, BigDecimal amountPaid, BigDecimal change) {}
