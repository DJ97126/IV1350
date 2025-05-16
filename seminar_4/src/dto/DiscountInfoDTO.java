package dto;

import model.Amount;

public record DiscountInfoDTO(String type, Amount value, String description) {}
