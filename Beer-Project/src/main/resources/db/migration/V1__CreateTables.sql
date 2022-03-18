CREATE TABLE if NOT EXISTS the_cheapest_brand (
id BIGINT NOT NULL AUTO_INCREMENT,
name_of_brand VARCHAR(250),
PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS filter_beers_by_type (
id BIGINT NOT NULL AUTO_INCREMENT,
ids_of_beers VARCHAR(250),
PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS get_ids_that_lack_from_specific_ingredient (
id BIGINT NOT NULL AUTO_INCREMENT,
ids_of_beers VARCHAR(250),
PRIMARY KEY (id)
);


CREATE TABLE if NOT EXISTS sort_all_beers_by_remaining_ingredient_ratio (
id BIGINT NOT NULL AUTO_INCREMENT,
ids_of_beers VARCHAR(250),
PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS list_beers_based_on_their_price_with_a_tip (
id BIGINT NOT NULL AUTO_INCREMENT,
ids_of_beers VARCHAR(250),
PRIMARY KEY (id)
);

CREATE TABLE if NOT EXISTS group_beers_by_brand (
id BIGINT NOT NULL AUTO_INCREMENT,
brand_of_beers VARCHAR(250),
ids_of_beers VARCHAR(250),
PRIMARY KEY (id)
);