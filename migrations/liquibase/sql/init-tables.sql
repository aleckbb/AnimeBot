--liquibase formatted sql

--changeset aleckbb:init_tables
create table if not exists anime(
    id BIGINT PRIMARY KEY,
    name TEXT,
    russian TEXT,
    url TEXT,
    kind TEXT,
    score NUMERIC(3, 2),
    status TEXT,
    episodes INTEGER,
    episodes_aired INTEGER,
    aired_on DATE,
    released_on DATE,
    rating TEXT,
    duration INTEGER,
    description TEXT,
    next_episode_at TIMESTAMPTZ,
    genres TEXT[],
    studious TEXT[]
);

create table if not exists subscription(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    anime_id BIGINT REFERENCES anime(id)
);

create table if not exists user_preferences(
    user_id BIGINT PRIMARY KEY,
    genre TEXT,
    kind TEXT,
    status TEXT
);

create table if not exists ai_log(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    request TEXT,
    response TEXT,
    created_at TIMESTAMP
);