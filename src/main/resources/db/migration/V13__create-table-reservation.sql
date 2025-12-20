CREATE TABLE IF NOT EXISTS reservations (
    id BIGserial PRIMARY KEY,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    daily_rate DECIMAL(10, 2) NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    discount_amount DECIMAL(10, 2),
    service_fee DECIMAL(10, 2),
    status_reservation VARCHAR(50) NOT NULL,
    source_reservation VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    client_id BIGSERIAL NOT NULL,
    room_id BIGSERIAL NOT NULL,

    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES client(id),
    CONSTRAINT fk_room FOREIGN KEY (room_id) REFERENCES room(id)
);