--liquibase formatted sql

--changeset rniyazov:1
CREATE TABLE IF NOT EXISTS news
(
    id      SERIAL PRIMARY KEY,
    date    TIMESTAMP          NOT NULL,
    title   VARCHAR(32) UNIQUE NOT NULL,
    text    VARCHAR(128)       NOT NULL,
    user_id INTEGER            NOT NULL
);