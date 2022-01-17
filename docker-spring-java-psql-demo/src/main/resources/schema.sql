DROP TABLE IF EXISTS product;

CREATE TABLE product
  (
     id                SERIAL PRIMARY KEY,
     brand_id          NUMERIC,
     name              TEXT,
     price             NUMERIC,
     stock             NUMERIC,
     status            TEXT
  );

DROP TABLE IF EXISTS brand;

CREATE TABLE brand
  (
     id       SERIAL PRIMARY KEY,
     name     TEXT,
     alphabet TEXT,
     status   TEXT
  );