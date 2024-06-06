--liquibase formatted sql

--changeset rniyazov:1
CREATE TABLE IF NOT EXISTS users
(
    id        SERIAL PRIMARY KEY,
    username  VARCHAR(32) UNIQUE NOT NULL,
    password  VARCHAR(128)       NOT NULL,
    firstname VARCHAR(32)        NOT NULL,
    lastname  VARCHAR(32)        NOT NULL,
    role      VARCHAR(16)        NOT NULL
    );