CREATE TABLE expenses (
                          id BIGSERIAL PRIMARY KEY,
                          amount NUMERIC(10, 2) NOT NULL CHECK (amount > 0),
                          category VARCHAR(50) NOT NULL,
                          description VARCHAR(200),
                          payment_method VARCHAR(30) NOT NULL,
                          currency VARCHAR(3) NOT NULL,        -- ← changed from CHAR(3)
                          expense_date DATE NOT NULL,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_expenses_date ON expenses(expense_date DESC);
CREATE INDEX idx_expenses_category ON expenses(category);