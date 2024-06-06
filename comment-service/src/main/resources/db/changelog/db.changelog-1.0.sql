--liquibase formatted sql

--changeset rniyazov:1
CREATE TABLE IF NOT EXISTS comment
(
    id      SERIAL PRIMARY KEY,
    date    TIMESTAMP    NOT NULL,
    text    VARCHAR(128) NOT NULL,
    user_id INTEGER      NOT NULL,
    news_id INTEGER      NOT NULL
);