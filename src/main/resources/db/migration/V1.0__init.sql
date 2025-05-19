CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE account
(
    id          UUID PRIMARY KEY     DEFAULT uuid_generate_v4(),
    iban        VARCHAR(30) NOT NULL,
    bic_swift    VARCHAR(10) NOT NULL,
    customer_id UUID        NOT NULL,
    voided      BOOLEAN     NOT NULL DEFAULT false,
    created_at  TIMESTAMP   NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP   NOT NULL DEFAULT now()
);

-- Index searchable fields
CREATE INDEX idx_account_iban ON account (iban);
CREATE INDEX idx_account_bic_swift ON account (bic_swift);
CREATE INDEX idx_account_customer_id ON account (customer_id);

