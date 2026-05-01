CREATE TABLE IF NOT EXISTS hotel_files (
    id BIGINT PRIMARY KEY,
    minio_key VARCHAR(255) NOT NULL UNIQUE,
    original_name VARCHAR(255),
    content_type VARCHAR(50),
    file_size BIGINT,
    hotel_id BIGINT,
    room_id BIGINT,
    user_id BIGINT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_room_id FOREIGN KEY (room_id) REFERENCES room (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES app_user (id) ON DELETE CASCADE
)