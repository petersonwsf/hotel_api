ALTER TABLE client
ADD COLUMN user_id BIGINT UNIQUE,
ADD CONSTRAINT fk_client_user 
    FOREIGN KEY (user_id) 
    REFERENCES app_user (id)
    ON DELETE CASCADE;