CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                       created_at timestamptz NOT NULL DEFAULT now(),
                       updated_at timestamptz NOT NULL DEFAULT now(),
                       created_by UUID,
                       updated_by UUID,

                       email TEXT NOT NULL,
                       status TEXT NOT NULL,                 -- ACTIVE | PENDING | DEACTIVATED | DELETED
                       full_name TEXT NOT NULL,
                       normalized_name TEXT NOT NULL
);

CREATE UNIQUE INDEX uk_users_email ON users ((lower(email)));
CREATE INDEX idx_users_normalized_name ON users (normalized_name);
