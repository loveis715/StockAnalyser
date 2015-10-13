CREATE TABLE STOCK
(
ID SERIAL NOT NULL,
NAME VARCHAR(16) NOT NULL,
CODE VARCHAR(16) NOT NULL,
TOTAL_VOLUME BIGINT NOT NULL,
STOCK_CATEGORY VARCHAR(16) NOT NULL,
LOCK_VERSION INTEGER NOT NULL DEFAULT 1,
PRIMARY KEY (ID)
);

CREATE TABLE STOCK_LISTING_TASK
(
ID SERIAL NOT NULL,
PERCENTAGE decimal(6, 4) NOT NULL,
TASK_STATE VARCHAR(16) NOT NULL,
START_TIME TIMESTAMP,
END_TIME TIMESTAMP,
LOCK_VERSION INTEGER NOT NULL DEFAULT 1,
PRIMARY KEY (ID)
);

CREATE TABLE STOCK_SYNCING_TASK
(
ID SERIAL NOT NULL,
PERCENTAGE decimal(6, 4) NOT NULL,
TASK_STATE VARCHAR(16) NOT NULL,
START_TIME TIMESTAMP NOT NULL,
END_TIME TIMESTAMP,
LISTING_TASK_FOR_SHANGHAI_ID INTEGER NOT NULL,
LISTING_TASK_FOR_SHENZHEN_ID INTEGER NOT NULL,
LOCK_VERSION INTEGER NOT NULL DEFAULT 1,
PRIMARY KEY (ID)
);

CREATE TABLE SCAN_TASK
(
ID SERIAL NOT NULL,
SCANNING_STOCK_NAME VARCHAR(16),
PERCENTAGE decimal(6, 4) NOT NULL,
SCAN_TYPE VARCHAR(20) NOT NULL,
TASK_STATE VARCHAR(16) NOT NULL,
START_TIME TIMESTAMP,
END_TIME TIMESTAMP,
LOCK_VERSION INTEGER NOT NULL DEFAULT 1,
PRIMARY KEY (ID)
);

CREATE TABLE SCAN_RESULT
(
ID SERIAL NOT NULL,
STOCK_ID INTEGER NOT NULL,
SCAN_TASK_ID INTEGER,
SCORE decimal(5, 2) NOT NULL,
TAGS VARCHAR(512) NOT NULL,
PRIMARY KEY (ID)
);

ALTER TABLE STOCK_SYNCING_TASK ADD FOREIGN KEY (LISTING_TASK_FOR_SHANGHAI_ID) REFERENCES STOCK_LISTING_TASK (ID);
ALTER TABLE STOCK_SYNCING_TASK ADD FOREIGN KEY (LISTING_TASK_FOR_SHENZHEN_ID) REFERENCES STOCK_LISTING_TASK (ID);

ALTER TABLE SCAN_RESULT ADD FOREIGN KEY (STOCK_ID) REFERENCES STOCK (ID);
ALTER TABLE SCAN_RESULT ADD FOREIGN KEY (SCAN_TASK_ID) REFERENCES SCAN_TASK (ID);