\c jewelry jewelry

CREATE TABLE STOCK
(
ID SERIAL NOT NULL,
NAME VARCHAR(16) NOT NULL,
CODE VARCHAR(16) NOT NULL,
LOCK_VERSION INTEGER NOT NULL DEFAULT 1,
PRIMARY KEY (ID)
);