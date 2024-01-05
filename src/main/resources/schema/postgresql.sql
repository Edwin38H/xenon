CREATE TABLE "{prefix}players" (
                                   "uuid"          VARCHAR(36) PRIMARY KEY NOT NULL,
                                   "username"      VARCHAR(16)             NOT NULL,
                                   "primary_group" VARCHAR(36)             NOT NULL
);