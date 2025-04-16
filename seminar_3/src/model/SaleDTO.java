package model;

import java.math.BigDecimal;

import integration.ItemDTO;

public record SaleDTO(ItemDTO[] boughtItems, BigDecimal change) {}
