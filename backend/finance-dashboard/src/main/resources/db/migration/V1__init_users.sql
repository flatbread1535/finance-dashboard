CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    hash_password VARCHAR(250) NOT NULL,
    phone_number VARCHAR(20),
    profile_picture_url VARCHAR(500),
    is_dark_mode BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);
