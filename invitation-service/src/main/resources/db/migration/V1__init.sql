CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE invitations (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             organization_id UUID NOT NULL,
                             invited_user_id UUID NOT NULL,
                             status TEXT NOT NULL DEFAULT 'PENDING',
                             created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                             updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                             created_by UUID,
                             updated_by UUID,
                             UNIQUE (organization_id, invited_user_id, status)
);
