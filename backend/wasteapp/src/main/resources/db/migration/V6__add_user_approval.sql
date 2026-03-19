-- Dodaje stupac za odobrenje korisnika
ALTER TABLE users ADD COLUMN is_approved TINYINT(1) NOT NULL DEFAULT 0;

--  rucno postavljanje admina da je odobren kako bi se mogao ulogirati u bazi
UPDATE users SET is_approved = 1 WHERE role = 'ADMIN';