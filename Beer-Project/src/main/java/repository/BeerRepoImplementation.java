package repository;

import beercatalog.*;
import controller.enums.Brand;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import service.BeerServiceImplementation;

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
    public Optional<List<BeerAndPrice>> getBeersAndPricesForTheCheapestBrandDb() {
        return Optional.of(jdbcTemplate.query("select * from beers;",
                (rs, rowNum) -> new BeerAndPrice(rs.getString("brand"), rs.getInt("price"))));
    }

    @Override
    public Optional<List<BeerIdWithIngredientRatio>> getBeerIdsWithIngrRatioForsortAllBeersByRemainingIngredientRatioDb() {
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
    public Optional<List<BeerAndPrice>> listBeersWithPriceForTipCalculationDb() {
        return Optional.of(jdbcTemplate.query("select * from beers;",
                (rs, rowNum) -> new BeerAndPrice(rs.getString("beer_id"), rs.getInt("price"))));
    }

    public String findBrandById(long id) {
        return jdbcTemplate.queryForObject(
                "select * from the_cheapest_brand where id = ?;",
                (rs, rowNum) ->
                        rs.getString("name_of_brand"),
                id);
    }

    public String findByIdInBeers(String id) {
        return jdbcTemplate.queryForObject(
                "select * from beers where beer_id = ?;",
                (rs, rowNum) ->
                        rs.getString("beer_name"),
                id);
    }

    public Double findByIdInIngredients(long id) {
        return jdbcTemplate.queryForObject(
                "select * from ingredients where id = ?;",
                (rs, rowNum) ->
                        rs.getDouble("ratio"),
                id);
    }

    public Double findByIdInIngredientsSum(String id) {
        return jdbcTemplate.queryForObject(
                "SELECT SUM(ratio) from ingredients where beer_id = ?;",
                (rs, rowNum) ->
                        rs.getDouble("SUM(ratio)"),
                id);
    }

    @Transactional
    public List<BeerAndPrice> updatePrice() {
        List<BeerAndPrice> prices = jdbcTemplate.query("select * from beers;",
                (rs, rowNum) -> new BeerAndPrice(rs.getString("beer_id"),
                        new BeerServiceImplementation().roundPrice(rs.getInt("price"))));
//                .stream().map(k -> new BeerServiceImplementation().roundPrice(k.getPrice()))
//                .toList();
        for (int i = 0; i < prices.size(); i++) {
            jdbcTemplate.update("update beers set price = ? where beer_id = ?;",
                    prices.get(i).getPrice(), prices.get(i).getBeer());
        }
        return prices;
    }

    @Override
    public boolean deleteBeerByIdDb(String beerId) {
        if (isABeerByIdInBeers(beerId)) {
            jdbcTemplate.update("delete from beers where beer_id = ?;", beerId);
            jdbcTemplate.update("delete from ingredients where beer_id = ?;", beerId);
            return true;
        }
        else{
            return false;
        }
    }

    public Beer findBeerByIdInBeers(String id) {
        Beer beer;
        try {
            beer = jdbcTemplate.queryForObject(
                    "select * from beers where beer_id = ?;",
                    (rs, rowNum) ->
                            new Beer(rs.getString("beer_id"),
                                    rs.getString("beer_name"),
                                    rs.getString("brand"),
                                    rs.getString("beer_type"),
                                    rs.getInt("price"),
                                    rs.getDouble("alcohol"),
                                    new ArrayList<>()),
                    id);
        } catch (EmptyResultDataAccessException erdae) {
            throw new IllegalArgumentException("No beer on this id: " + id, erdae);
        }
        if (beer != null) {
            beer.addIngredients(findIngredientsById(id));
        }
//        else {
//            throw new IllegalArgumentException("No beer on this id: " + id);
//        }
        return beer;
    }

    public List<Ingredient> findIngredientsById(String id) {
        return jdbcTemplate.query(
                "select * from ingredients where beer_id = ?;",
                (rs, rowNum) ->
                        new Ingredient(rs.getString("ingredient_name"),
                                rs.getDouble("ratio")),
                id);
    }

    public boolean isABeerByIdInBeers(String id) {
        try {
            String number = jdbcTemplate.queryForObject(
                    "select * from beers where beer_id = ?;",
                    (rs, rowNum) ->
                            rs.getString("beer_id"),
                    id);
        } catch (EmptyResultDataAccessException erdae) {
            return false;
        }
        return true;
    }
}
