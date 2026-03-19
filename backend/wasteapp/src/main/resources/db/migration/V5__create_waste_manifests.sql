-- Flyway V5: waste_manifests (prateći listovi)
-- PROGRAMSKI ENTITET: SQL migracija
-- SVRHA: Dokument koji nastaje iz radnog naloga i služi kao prateći list otpada

CREATE TABLE IF NOT EXISTS waste_manifests (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,

  manifest_number VARCHAR(50) NOT NULL UNIQUE,  -- npr. WM-2026-000001
  work_order_id BIGINT NOT NULL UNIQUE,         -- jedan manifest po work orderu

  issued_at DATETIME NOT NULL,                  -- datum izdavanja pratećeg lista
  note VARCHAR(500) NULL,

  created_at DATETIME NOT NULL
);

ALTER TABLE waste_manifests
  ADD CONSTRAINT fk_waste_manifests_work_order
  FOREIGN KEY (work_order_id) REFERENCES work_orders(id);

CREATE INDEX idx_waste_manifests_manifest_number ON waste_manifests(manifest_number);