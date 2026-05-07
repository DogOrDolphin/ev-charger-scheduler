DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS availability_slots;
DROP TABLE IF EXISTS chargers;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(200) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE chargers (
    charger_id SERIAL PRIMARY KEY,
    label VARCHAR(100) NOT NULL,
    location VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE availability_slots (
  slot_id SERIAL PRIMARY KEY,
  charger_id INT NOT NULL REFERENCES chargers(charger_id),
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
  version INT NOT NULL DEFAULT 0,
  CONSTRAINT slot_time_valid CHECK (start_time < end_time),
  CONSTRAINT slot_unique UNIQUE (charger_id, start_time, end_time)
);

CREATE TABLE appointments (
    appointment_id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(user_id),
    slot_id INT NOT NULL UNIQUE REFERENCES availability_slots(slot_id),
    status VARCHAR(20) NOT NULL DEFAULT 'BOOKED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);