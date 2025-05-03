package model;

import java.math.BigDecimal;

/**
 * This serves as the payment class that stores the amount of money paid by the customer.
 */
public record Payment(BigDecimal amount) {}