CREATE TABLE IF NOT EXISTS user_actions (
    id BIGSERIAL PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    action_timestamp TIMESTAMP NOT NULL,
    user_id BIGSERIAL NOT NULL,

    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES app_user(id)
)