-- Flyway V2: reference data - vrste otpada
-- PROGRAMSKI ENTITET: SQL migracija
-- SVRHA: Tablica šifrarnika / reference data za vrste otpada (admin održava)

CREATE TABLE IF NOT EXISTS waste_types (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(50) NOT NULL UNIQUE,        -- npr. PLASTIC, PAPER, BIO
  name VARCHAR(255) NOT NULL,              -- npr. Plastika, Papir
  description VARCHAR(500) NULL,
  is_active TINYINT(1) NOT NULL DEFAULT 1, -- soft enable/disable
  created_at DATETIME NOT NULL
);
