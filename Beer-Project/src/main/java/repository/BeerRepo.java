package repository;

import beercatalog.Beer;
import beercatalog.Ingredient;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

public class BeerRepo {
    private static final String PATH_PROPERTIES = "/beerstore.properties";
    private MariaDbDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public BeerRepo() {
        this.dataSource = init();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public MariaDbDataSource init() {
        Properties prop = new Properties();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(BeerRepo.class.getResourceAsStream(PATH_PROPERTIES)))) {
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

    public void saveBeers(List<Beer> beers) {
        for (Beer beer : beers) {
            jdbcTemplate.update("insert into beers (beer_id, beer_name, brand, beer_type, price, alcohol) values (?,?,?,?,?,?);",
                    beer.getId(), beer.getName(), beer.getBrand(), beer.getType(), beer.getPrice(), beer.getAlcohol());
            saveIngredients(beer);
        }
    }

    public void saveIngredients(Beer beer) {
        for (Ingredient ingredient : beer.getIngredients()) {
            jdbcTemplate.update("insert into ingredients (beer_id, ingredient_name, ratio) values (?,?,?);",
                    beer.getId(), ingredient.getName(), ingredient.getRatio());
        }
    }
}
