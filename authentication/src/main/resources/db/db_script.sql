CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    account_id UUID UNIQUE,
    username VARCHAR(50) NOT NULL CONSTRAINT unique_username UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);

CREATE TABLE user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_name VARCHAR(50) NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);