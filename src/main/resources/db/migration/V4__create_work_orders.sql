-- Flyway V4: work_orders (radni nalozi)
-- PROGRAMSKI ENTITET: SQL migracija
-- SVRHA: Glavni procesni entitet za zahtjev/odvoz otpada

CREATE TABLE IF NOT EXISTS work_orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,

  user_id BIGINT NOT NULL,              -- tko je kreirao zahtjev
  waste_type_id BIGINT NOT NULL,        -- vrsta otpada
  collection_point_id BIGINT NOT NULL,  -- lokacija

  quantity DECIMAL(12,3) NOT NULL,      -- npr. 12.500
  unit VARCHAR(10) NOT NULL,            -- KG, T, M3

  status VARCHAR(20) NOT NULL,          -- CREATED, SCHEDULED, COMPLETED, CANCELLED
  requested_at DATETIME NOT NULL,       -- kada je korisnik poslao zahtjev
  scheduled_for DATETIME NULL,          -- kada je planiran odvoz
  completed_at DATETIME NULL,           -- kada je odvoz odrađen

  note VARCHAR(500) NULL,

  created_at DATETIME NOT NULL
);

-- FK veze (referencijalni integritet)
ALTER TABLE work_orders
  ADD CONSTRAINT fk_work_orders_user
  FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE work_orders
  ADD CONSTRAINT fk_work_orders_waste_type
  FOREIGN KEY (waste_type_id) REFERENCES waste_types(id);

ALTER TABLE work_orders
  ADD CONSTRAINT fk_work_orders_collection_point
  FOREIGN KEY (collection_point_id) REFERENCES collection_points(id);

-- Indeksi za brže liste
CREATE INDEX idx_work_orders_user_id ON work_orders(user_id);
CREATE INDEX idx_work_orders_status ON work_orders(status);
CREATE INDEX idx_work_orders_scheduled_for ON work_orders(scheduled_for);