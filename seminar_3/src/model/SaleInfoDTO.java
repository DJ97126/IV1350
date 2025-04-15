package model;

import integration.ItemDTO;

public record SaleInfoDTO(ItemDTO currentItem, double totalPrice, double totalVat) {}
