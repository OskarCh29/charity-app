INSERT INTO currency VALUES
('USD'),('EUR'),('PLN'),('CHF'),('AUS'),('YEN'),('GBP');

INSERT INTO charity_account (currency_symbol) VALUES ('PLN');
INSERT INTO charity_account (currency_symbol) VALUES ('EUR');
INSERT INTO charity_account (currency_symbol) VALUES ('USD');

INSERT INTO fundraising_event (name,account_id) VALUES
('WOSP',1),('UNICEF',2),('Red Cross',3);