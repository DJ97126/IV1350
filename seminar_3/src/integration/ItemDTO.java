package integration;

import java.math.BigDecimal;

public record ItemDTO(String id, String name, BigDecimal price, BigDecimal vat, String description) {}
