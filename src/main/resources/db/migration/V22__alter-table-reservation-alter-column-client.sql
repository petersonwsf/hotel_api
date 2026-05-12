ALTER TABLE reservations
DROP CONSTRAINT IF EXISTS fk_client;

ALTER TABLE reservations
RENAME COLUMN client_id TO user_id;

ALTER TABLE reservations
ALTER COLUMN user_id TYPE BIGINT;

ALTER TABLE reservations
ADD CONSTRAINT fk_user
FOREIGN KEY (user_id) REFERENCES app_user(id);