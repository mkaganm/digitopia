-- Create per-service databases if not exists (all owned by postgres)
DO $$
BEGIN
  IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'userdb') THEN
    CREATE DATABASE userdb OWNER postgres;
END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'orgdb') THEN
    CREATE DATABASE orgdb OWNER postgres;
END IF;
  IF NOT EXISTS (SELECT 1 FROM pg_database WHERE datname = 'invdb') THEN
    CREATE DATABASE invdb OWNER postgres;
END IF;
END
$$;

-- Ensure public schema ownership (optional but clean)
\connect userdb
ALTER SCHEMA public OWNER TO postgres;

\connect orgdb
ALTER SCHEMA public OWNER TO postgres;

\connect invdb
ALTER SCHEMA public OWNER TO postgres;

\connect postgres
