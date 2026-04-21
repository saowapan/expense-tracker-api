package com.saowapan.expense_tracker_api.service;

import com.saowapan.expense_tracker_api.dto.ExpenseRequest;
import com.saowapan.expense_tracker_api.dto.ExpenseResponse;
import com.saowapan.expense_tracker_api.exception.ResourceNotFoundException;
import com.saowapan.expense_tracker_api.model.Expense;
import com.saowapan.expense_tracker_api.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getAll() {
        return expenseRepository
                .findAllByOrderByExpenseDateDescIdDesc()
                .stream()
                .map(ExpenseResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ExpenseResponse> getByDateRange(LocalDate from, LocalDate to) {
        return expenseRepository
                .findByExpenseDateBetweenOrderByExpenseDateDescIdDesc(from, to)
                .stream()
                .map(ExpenseResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ExpenseResponse getById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expense not found with id: " + id));
        return ExpenseResponse.fromEntity(expense);
    }

    public ExpenseResponse create(ExpenseRequest request) {
        Expense expense = Expense.builder()
                .amount(request.amount())
                .category(request.category())
                .description(request.description())
                .paymentMethod(request.paymentMethod())
                .currency(request.currency())
                .expenseDate(request.expenseDate())
                .build();

        Expense saved = expenseRepository.save(expense);
        return ExpenseResponse.fromEntity(saved);
    }

    public ExpenseResponse update(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Expense not found with id: " + id));

        expense.setAmount(request.amount());
        expense.setCategory(request.category());
        expense.setDescription(request.description());
        expense.setPaymentMethod(request.paymentMethod());
        expense.setCurrency(request.currency());
        expense.setExpenseDate(request.expenseDate());

        return ExpenseResponse.fromEntity(expense);
    }

    public void delete(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

}
