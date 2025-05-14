package dto;

import model.Amount;

public record SaleInfoDTO(ItemDTO currentItem, Amount totalPrice, Amount totalVat) {}
