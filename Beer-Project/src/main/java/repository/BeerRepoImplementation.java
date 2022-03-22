package repository;

import beercatalog.*;
import controller.Brand;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

public class BeerRepoImplementation implements BeerRepo {
    private static final String PATH_PROPERTIES = "/beerstore.properties";
    private MariaDbDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public BeerRepoImplementation() {
        this.dataSource = init();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public MariaDbDataSource init() {
        Properties prop = new Properties();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(BeerRepoImplementation.class.getResourceAsStream(PATH_PROPERTIES)))) {
            prop.load(br);
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl(prop.getProperty("url"));
            dataSource.setUser(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));
            return dataSource;
        } catch (IOException | SQLException ex) {
            throw new IllegalStateException("Cannot reach file", ex);
        }
    }

    @Override
    public void saveBeers(List<Beer> beers) {
        for (Beer beer : beers) {
            jdbcTemplate.update("insert into beers (beer_id, beer_name, brand, beer_type, price, alcohol) values (?,?,?,?,?,?);",
                    beer.getId(), beer.getName(), beer.getBrand(), beer.getType(), beer.getPrice(), beer.getAlcohol());
            saveIngredients(beer);
        }
    }

    @Override
    public void saveIngredients(Beer beer) {
        for (Ingredient ingredient : beer.getIngredients()) {
            jdbcTemplate.update("insert into ingredients (beer_id, ingredient_name, ratio) values (?,?,?);",
                    beer.getId(), ingredient.getName(), ingredient.getRatio());
        }
    }

    @Override
    public Optional<List<String>> filterBeersByBeerTypeDb(String type) {
        return Optional.of(jdbcTemplate.query("select * from beers where beer_type = ?;",
                (rs, rowNum) -> rs.getString("beer_id"), type));
    }

    @Override
    public Optional<List<String>> getIdsThatLackSpecificIngredientDb(String ingredient) {
        return Optional.of(jdbcTemplate.query("select * from ingredients where ingredient_name = ? AND ratio = 0;",
                (rs, rowNum) -> rs.getString("beer_id"), ingredient));
    }

    @Override
    public Optional<List<BeerAndPrice>> getTheCheapestBrandDb() {
        return Optional.of(jdbcTemplate.query("select * from beers;",
                (rs, rowNum) -> new BeerAndPrice(rs.getString("brand"), rs.getInt("price"))));
    }

    @Override
    public Optional<List<BeerIdWithIngredientRatio>> sortAllBeersByRemainingIngredientRatioDb() {
        return Optional.of(jdbcTemplate.query("select * from ingredients;",
                (rs, rowNum) -> new BeerIdWithIngredientRatio(rs.getString("beer_id"), rs.getDouble("ratio"))));
    }

    @Override
    public List<BrandsWithBeers> groupBeersByBrandDb() {
        List<BrandsWithBeers> brandsWithBeers = new ArrayList<>();
        for (Brand brand : Brand.values()) {
            brandsWithBeers.add(new BrandsWithBeers(brand.getName(), jdbcTemplate.query("select * from beers WHERE brand = ?;",
                    (rs, rowNum) -> rs.getString("beer_id"), brand.getName())));
        }
        return brandsWithBeers;
    }

    @Override
    public Optional<List<BeerAndPrice>> listBeersBasedOnTheirPriceWithATipDb() {
        return Optional.of(jdbcTemplate.query("select * from beers;",
                (rs, rowNum) -> new BeerAndPrice(rs.getString("beer_id"), rs.getInt("price"))));
    }
}