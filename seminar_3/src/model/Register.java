package model;

import java.math.BigDecimal;

public class Register {

    BigDecimal amount;

    public void updateAmount(BigDecimal amount) {

        this.amount = this.amount.add(amount);
    }
}
