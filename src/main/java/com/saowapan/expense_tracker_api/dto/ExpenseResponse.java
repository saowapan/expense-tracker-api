package com.saowapan.expense_tracker_api.dto;
import com.saowapan.expense_tracker_api.model.Expense;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


public record ExpenseResponse (
    Long id,
    BigDecimal amount,
    String category,
    String description,
    String paymentMethod,
    String currency,
    LocalDate expenseDate,
    LocalDateTime createAt
){
    // Static factory - map entity -> response
    public static ExpenseResponse fromEntity(Expense e) {
        return new ExpenseResponse(
            e.getId(),
            e.getAmount(),
            e.getCategory(),
            e.getDescription(),
            e.getPaymentMethod(),
            e.getCurrency(),
            e.getExpenseDate(),
            e.getCreatedAt()
        );
    }
}
