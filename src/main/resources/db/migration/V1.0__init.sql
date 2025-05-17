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
