INSERT INTO users (name, email, role)
VALUES ('Demo Employee', 'demo@company.com', 'CUSTOMER');

INSERT INTO chargers (label, location, status)
VALUES
('Garage A - Charger 1', 'HQ Garage A', 'ACTIVE'),
('Garage A - Charger 2', 'HQ Garage A', 'ACTIVE');

INSERT INTO availability_slots (charger_id, start_time, end_time, status)
VALUES
(1, '2026-03-12 09:00:00', '2026-03-12 09:30:00', 'OPEN'),
(1, '2026-03-12 09:30:00', '2026-03-12 10:00:00', 'OPEN'),
(2, '2026-03-12 09:00:00', '2026-03-12 09:30:00', 'OPEN'),
(2, '2026-03-12 09:30:00', '2026-03-12 10:00:00', 'OPEN');