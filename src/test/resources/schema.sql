CREATE TABLE currency (
symbol VARCHAR(3) PRIMARY KEY);

CREATE TABLE charity_account(
id BIGINT PRIMARY KEY AUTO_INCREMENT,
currency_symbol VARCHAR(3) NOT NULL,
balance DECIMAL(19,2) NOT NULL DEFAULT 0.0,
CONSTRAINT fk_currency FOREIGN KEY (currency_symbol) REFERENCES currency(symbol)
);

CREATE TABLE fundraising_event(
id BIGINT PRIMARY KEY AUTO_INCREMENT,
name VARCHAR(100) NOT NULL UNIQUE,
account_id BIGINT UNIQUE NOT NULL,
CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES charity_account(id)
);

CREATE TABLE collection_box(
id BIGINT PRIMARY KEY AUTO_INCREMENT,
fundraising_event_id BIGINT,
CONSTRAINT fk_event FOREIGN KEY (fundraising_event_id) REFERENCES fundraising_event(id)
);

CREATE TABLE donation(
id BIGINT PRIMARY KEY AUTO_INCREMENT,
amount DECIMAL(19,2) NOT NULL CHECK (amount > 0 ),
currency_symbol VARCHAR(3) NOT NULL,
collection_box_id BIGINT NOT NULL,
CONSTRAINT fk_box FOREIGN KEY (collection_box_id) REFERENCES collection_box(id),
CONSTRAINT fk_currency_symbol FOREIGN KEY (currency_symbol) REFERENCES currency(symbol)
);

INSERT INTO currency VALUES
('USD'),('EUR'),('PLN'),('CHF'),('AUS'),('YEN'),('GBP');