package dto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import model.Amount;

public record SaleDTO(LocalDateTime saleDateTime, ArrayList<ItemDTO> boughtItems, Amount totalPrice,
		Amount totalVat, Amount amountPaid, Amount change, Amount discountedPrice) {}
