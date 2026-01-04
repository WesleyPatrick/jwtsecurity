CREATE TABLE refresh_tokens (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    hashed_token VARCHAR(100) NOT NULL,
    lookup_hash VARCHAR(64) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT uk_refresh_user UNIQUE (user_id),
    CONSTRAINT uk_refresh_lookup UNIQUE (lookup_hash),

    CONSTRAINT fk_refresh_user
        FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE
);