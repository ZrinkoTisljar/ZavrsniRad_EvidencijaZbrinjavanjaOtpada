-- Flyway V3: reference data - collection points (lokacije)
-- PROGRAMSKI ENTITET: SQL migracija
-- SVRHA: Tablica lokacija (reciklažna dvorišta / odlagališta / punktovi)

CREATE TABLE IF NOT EXISTS collection_points (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,

  name VARCHAR(255) NOT NULL,              -- naziv lokacije
  street VARCHAR(255) NULL,                -- ulica i broj
  city VARCHAR(100) NOT NULL,              -- grad/općina
  postal_code VARCHAR(20) NULL,            -- poštanski broj

  latitude DECIMAL(10,7) NULL,             -- za mapu (opcionalno)
  longitude DECIMAL(10,7) NULL,

  is_active TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL
);

-- Koristan indeks za pretragu po gradu
CREATE INDEX idx_collection_points_city ON collection_points(city);
