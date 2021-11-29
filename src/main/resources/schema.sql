DROP TABLE IF EXISTS USER;

CREATE TABLE ACCOUNT(
ACCOUNT_ID INTEGER AUTO_INCREMENT PRIMARY KEY,
amount INTEGER NOT NULL
);

CREATE TABLE ADDRESS(
ADDRESS_ID INTEGER AUTO_INCREMENT PRIMARY KEY,
CITY VARCHAR(20),
STATE VARCHAR(20),
PINCODE VARCHAR(20),
HOUSENO VARCHAR(20)
);

CREATE TABLE USER (
USER_ID INTEGER AUTO_INCREMENT  PRIMARY KEY,
NAME VARCHAR(50) NOT NULL,
GENDER VARCHAR(20) NOT NULL,
ADDRESS_ID INTEGER NOT NULL,
MOBILE_NO VARCHAR(10) NOT NULL,
ACCOUNT_ID INTEGER NOT NULL,
FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(ACCOUNT_ID),
FOREIGN KEY (ADDRESS_ID) REFERENCES ADDRESS(ADDRESS_ID)
);

CREATE TABLE TRANSACTION(
TRANSACTION_ID INTEGER AUTO_INCREMENT PRIMARY KEY,
FROM_USER_ID INTEGER NOT NULL,
TO_USER_ID INTEGER NOT NULL,
STATUS VARCHAR(10),
AMOUNT INTEGER NOT NULL,
DATETIME TIMESTAMP NOT NULL,
FOREIGN KEY (FROM_USER_ID) REFERENCES USER(USER_ID),
FOREIGN KEY (TO_USER_ID) REFERENCES USER(USER_ID)
);

CREATE TABLE USER_CREDENTIALS(
    ID INTEGER AUTO_INCREMENT PRIMARY KEY,
    USER_ID INTEGER NOT NULL,
    USERNAME VARCHAR(20) NOT NULL,
    PASSWORD VARCHAR(20) NOT NULL,
    FOREIGN KEY (USER_ID) REFERENCES USER(USER_ID)
);