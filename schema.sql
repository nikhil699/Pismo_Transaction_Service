-- Dev convenience: drop existing (safe for local)
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS operation_types;
DROP TABLE IF EXISTS accounts;

-- accounts
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    document_number VARCHAR(32) UNIQUE NOT NULL
);

-- operation_types
CREATE TABLE operation_types (
    id SMALLINT PRIMARY KEY,
    description VARCHAR(64) UNIQUE NOT NULL
);

-- transactions
CREATE TABLE transactions (
    id BIGSERIAL PRIMARY KEY,
    account_id BIGINT NOT NULL REFERENCES accounts(id),
    operation_type_id SMALLINT NOT NULL REFERENCES operation_types(id),
    amount NUMERIC(19,2) NOT NULL,
    event_date TIMESTAMPTZ NOT NULL
);

-- helpful indexes
CREATE INDEX IF NOT EXISTS idx_transactions_account_id ON transactions(account_id);
CREATE INDEX IF NOT EXISTS idx_transactions_event_date ON transactions(event_date);
