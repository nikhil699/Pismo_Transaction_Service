-- Drop (fresh start)
DROP TABLE IF EXISTS transactions CASCADE;
DROP TABLE IF EXISTS accounts CASCADE;
DROP TABLE IF EXISTS operation_types CASCADE;

-- ACCOUNTS
CREATE TABLE IF NOT EXISTS accounts (
  id BIGSERIAL PRIMARY KEY,
  document_number VARCHAR(20) NOT NULL UNIQUE
);

-- OPERATION TYPES (master)
CREATE TABLE IF NOT EXISTS operation_types (
  id SMALLINT PRIMARY KEY,
  description VARCHAR(64) NOT NULL UNIQUE
);

-- TRANSACTIONS
CREATE TABLE IF NOT EXISTS transactions (
  id BIGSERIAL PRIMARY KEY,
  account_id BIGINT NOT NULL,
  operation_type_id SMALLINT NOT NULL,
  amount NUMERIC(15,2) NOT NULL,
  event_date TIMESTAMP NOT NULL DEFAULT NOW(),

  CONSTRAINT fk_transactions_account
    FOREIGN KEY (account_id) REFERENCES accounts(id),

  CONSTRAINT fk_transactions_operation_type
    FOREIGN KEY (operation_type_id) REFERENCES operation_types(id),

  CONSTRAINT chk_transactions_amount_nonzero CHECK (amount <> 0)
);

CREATE INDEX IF NOT EXISTS idx_transactions_account_id
  ON transactions (account_id);
CREATE INDEX IF NOT EXISTS idx_transactions_operation_type_id
  ON transactions (operation_type_id);
