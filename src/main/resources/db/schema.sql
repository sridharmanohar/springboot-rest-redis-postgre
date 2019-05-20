CREATE TABLE exchange_list (
 id integer PRIMARY KEY,
 exchange_code text NOT NULL,
 country text NOT NULL,
 name text NOT NULL,
 active bool NOT NULL,
 created_date timestamp NOT NULL,
 created_by text NOT NULL,
 last_updated_date timestamp NOT NULL,
 last_updated_by text NOT NULL
);

CREATE SEQUENCE exchange_list_seq_id OWNED BY exchange_list.id;

INSERT INTO exchange_list (id,exchange_code,country,name,active,created_date,created_by,last_updated_date,last_updated_by)
VALUES (1,'NSE','India','National Stock Exchange',true,current_timestamp,'admin',current_timestamp,'admin');

INSERT INTO exchange_list (id,exchange_code,country,name,active,created_date,created_by,last_updated_date,last_updated_by)
VALUES (2,'BSE','India','Bombay Stock Exchange',true,current_timestamp,'admin',current_timestamp,'admin');

INSERT INTO exchange_list (id,exchange_code,country,name,active,created_date,created_by,last_updated_date,last_updated_by)
VALUES (3,'NASDAQ','USA','Nasdaq',true,current_timestamp,'admin',current_timestamp,'admin');

INSERT INTO exchange_list (id,exchange_code,country,name,active,created_date,created_by,last_updated_date,last_updated_by)
VALUES (4,'NYSE','USA','New York Stock Exchange',true,current_timestamp,'admin',current_timestamp,'admin');

INSERT INTO exchange_list (id,exchange_code,country,name,active,created_date,created_by,last_updated_date,last_updated_by)
VALUES (5,'HOSE','Vietnam','Ho Chi Minh City Stock Exchange',true,current_timestamp,'admin',current_timestamp,'admin');


CREATE TABLE stock_list (
 id integer PRIMARY KEY,
 exchange_code_id integer REFERENCES exchange_list (id),
 stock_code text NOT NULL,
 stock_listed_date date,
 status text NOT NULL,
 active bool NOT NULL,
 company_name text NOT NULL,
 sectoral_index text
); 

CREATE SEQUENCE stock_list_seq_id OWNED BY stock_list.id;

INSERT INTO stock_list (id,exchange_code_id,stock_code,status,active,company_name,sectoral_index) VALUES (1,1,'BPCL','Listed',true,'Bharat Petroleum Corporation Limited','NIFTY 500');

INSERT INTO stock_list (id,exchange_code_id,stock_code,status,active,company_name,sectoral_index) VALUES (2,1,'RELIANCE','Listed',true,'Reliance Industries Limited','NIFTY 500');

INSERT INTO stock_list (id,exchange_code_id,stock_code,status,active,company_name,sectoral_index) VALUES (3,1,'BANCOINDIA','Listed',true,'Banco Products (I) Limited','NIFT AUTO');

INSERT INTO stock_list (id,exchange_code_id,stock_code,status,active,company_name,sectoral_index) VALUES (4,2,'ICICIBANK','Listed',true,'ICICI BANK LTD.','S&P BSE SENSEX');

INSERT INTO stock_list (id,exchange_code_id,stock_code,status,active,company_name,sectoral_index) VALUES (5,2,'IONEXCHANG','Listed',true,'ION EXCHANGE (INDIA) LTD.','S&P BSE SmallCap');

INSERT INTO stock_list (id,exchange_code_id,stock_code,status,active,company_name) VALUES (6,4,'A','Listed',true,'AGILENT TECHNOLOGIES INC');

INSERT INTO stock_list (id,exchange_code_id,stock_code,status,active,company_name) VALUES (7,4,'AA','Listed',true,'ALCOA CORPORATION');

INSERT INTO stock_list (id,exchange_code_id,stock_code,status,active,company_name) VALUES (8,3,'LOVE','Listed',true,'The Lovesac Company');

INSERT INTO stock_list (id,exchange_code_id,stock_code,status,active,company_name) VALUES (9,5,'ABCD','Listed',true,'The ABCD Company');














