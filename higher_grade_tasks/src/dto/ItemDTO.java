package dto;

import model.Amount;

public record ItemDTO(String id, String name, Amount price, Amount vat, String description) {}
