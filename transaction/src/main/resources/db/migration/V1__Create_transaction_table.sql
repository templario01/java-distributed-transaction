-- Create the transaction table
CREATE TABLE transaction (
    id SERIAL,
    transaction_external_id UUID UNIQUE,
    account_external_id_debit VARCHAR(255) NOT NULL,
    account_external_id_credit VARCHAR(255) NOT NULL,
    transaction_status VARCHAR(10),
    transfer_type_id INTEGER NOT NULL,
    value DOUBLE PRECISION NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT transaction_pk PRIMARY KEY (id)
);
