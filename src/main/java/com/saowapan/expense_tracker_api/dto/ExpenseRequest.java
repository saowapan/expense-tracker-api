package com.saowapan.expense_tracker_api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseRequest (
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    BigDecimal amount,

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category must be 50 characters or less")
    String category,

    @Size(max = 200, message = "Description must be 200 character or less")
    String description,

    @NotBlank(message = "Payment method is required")
    @Size(max =30, message = "Payment method must be 30 character or less")
    String paymentMethod,

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be exactly 3 character")
    String currency,

    @NotNull(message = "Expense date is required")
    LocalDate expenseDate
) {}
