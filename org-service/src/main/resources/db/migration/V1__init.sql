CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE organizations (
                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                               updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                               created_by UUID,
                               updated_by UUID,
                               owner_id UUID NOT NULL,
                               name TEXT NOT NULL,
                               normalized_name TEXT NOT NULL,
                               status TEXT NOT NULL DEFAULT 'ACTIVE'
);

CREATE UNIQUE INDEX uk_orgs_name ON organizations ((lower(name)));
CREATE INDEX idx_orgs_normalized_name ON organizations (normalized_name);
