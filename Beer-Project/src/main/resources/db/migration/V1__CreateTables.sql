CREATE TABLE if NOT EXISTS beers (
id BIGINT NOT NULL AUTO_INCREMENT,
beer_id VARCHAR(250),
beer_name VARCHAR(250),
brand VARCHAR(250),
beer_type VARCHAR(250),
price INT,
alcohol FLOAT,
PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS ingredients (
id BIGINT NOT NULL AUTO_INCREMENT,
beer_id VARCHAR(250),
ingredient_name VARCHAR(250),
ratio FLOAT,
PRIMARY KEY (id)
);
