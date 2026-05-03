-- Flyway V1: početna shema (users)
-- PROGRAMSKI ENTITET: SQL migracija
-- SVRHA: Kreira tablicu users za autentifikaciju i evidenciju korisnika

CREATE TABLE IF NOT EXISTS users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL,
  user_type VARCHAR(20) NOT NULL,
  full_name VARCHAR(255),
  company_name VARCHAR(255),
  oib VARCHAR(32),
  address VARCHAR(255) NOT NULL,
  phone VARCHAR(64),
  created_at DATETIME NOT NULL
);
