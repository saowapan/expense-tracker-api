package com.saowapan.expense_tracker_api.repository;

import com.saowapan.expense_tracker_api.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByExpenseDateBetweenOrderByExpenseDateDescIdDesc(
            LocalDate from,
            LocalDate to
    );

    List<Expense> findAllByOrderByExpenseDateDescIdDesc();
}