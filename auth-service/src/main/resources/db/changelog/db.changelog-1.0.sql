--liquibase formatted sql

--changeset rniyazov:1
CREATE TABLE IF NOT EXISTS token
(
    id         SERIAL PRIMARY KEY,
    expired    BOOLEAN      NOT NULL,
    revoked    BOOLEAN      NOT NULL,
    token      VARCHAR(255) NOT NULL,
    token_type VARCHAR(24)  NOT NULL,
    user_id    INTEGER      NOT NULL
);