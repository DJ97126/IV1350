package model;

import java.math.BigDecimal;

import integration.ItemDTO;

public record SaleInfoDTO(ItemDTO currentItem, BigDecimal totalPrice, BigDecimal totalVat) {}
