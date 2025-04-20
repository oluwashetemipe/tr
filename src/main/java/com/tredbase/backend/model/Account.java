package com.tredbase.backend.model;

import com.tredbase.backend.exceptions.BadRequestException;
import com.tredbase.backend.exceptions.InsufficientFundsException;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@MappedSuperclass
@Data
public class Account {

    @Column(nullable = false)
    protected BigDecimal balance = BigDecimal.ZERO;

    public void deposit(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.add(amount);
    }

    public void withdraw(BigDecimal amount) {
        validateAmount(amount);
        this.balance = this.balance.subtract(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new BadRequestException("Amount must not be null");
        }
    }


}
