package dto;

import java.time.LocalDateTime;
import java.util.List;

import model.Amount;

public record SaleDTO(LocalDateTime saleDateTime, List<ItemDTO> boughtItems, Amount totalPrice,
		Amount totalVat, Amount amountPaid, Amount change) {}
