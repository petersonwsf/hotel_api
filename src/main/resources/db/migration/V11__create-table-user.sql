CREATE TABLE IF NOT EXISTS app_user (
    id BIGSERIAL PRIMARY KEY,
    name varchar(255) NOT NULL,
    login varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    papel varchar(50) NOT NULL
)