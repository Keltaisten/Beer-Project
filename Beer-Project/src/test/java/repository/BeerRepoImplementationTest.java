package repository;

import beercatalog.Beer;
import beercatalog.Ingredient;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.dao.EmptyResultDataAccessException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class BeerRepoImplementationTest {

    BeerRepoImplementation beerRepository = new BeerRepoImplementation();
    List<Beer> beers = new ArrayList<>();

    @BeforeEach
    void initTest() {
        Properties prop = new Properties();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(BeerRepoImplementation.class.getResourceAsStream("/beerstore.properties")))) {
            prop.load(br);
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl(prop.getProperty("url"));
            dataSource.setUser(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));
            beerRepository.init();
            Flyway fw = Flyway.configure().dataSource(dataSource).load();
            fw.clean();
            fw.migrate();
        } catch (IOException | SQLException ex) {
            throw new IllegalStateException("Cannot reach file", ex);
        }

        Beer beer1 = new Beer("ccw-1", "Coding Challenge White", "Coding Challenge Brewery",
                "White", 4645, 0.119,
                Arrays.asList(
                        new Ingredient("salt", 0.002),
                        new Ingredient("sugar", 0.014),
                        new Ingredient("barley", 0.025),
                        new Ingredient("wheat", 0.014),
                        new Ingredient("corn", 0)));
        Beer beer2 = new Beer("sw-1", "Share White", "Share",
                "White", 6474, 0.126,
                Arrays.asList(
                        new Ingredient("salt", 0.005),
                        new Ingredient("sugar", 0.021),
                        new Ingredient("barley", 0.013),
                        new Ingredient("wheat", 0.014),
                        new Ingredient("corn", 0)));
        Beer beer3 = new Beer("lupa-1", "Level Up Pale Ale", "Level Up Brewery",
                "Pale Ale", 3528, 0.15,
                Arrays.asList(
                        new Ingredient("salt", 0.002),
                        new Ingredient("sugar", 0.023),
                        new Ingredient("barley", 0.023),
                        new Ingredient("wheat", 0),
                        new Ingredient("corn", 0)));
        Beer beer4 = new Beer("spa-1", "Share Pale Ale", "Share",
                "Pale Ale", 9775, 0.142,
                Arrays.asList(
                        new Ingredient("salt", 0.003),
                        new Ingredient("sugar", 0.019),
                        new Ingredient("barley", 0.013),
                        new Ingredient("wheat", 0),
                        new Ingredient("corn", 0)));
        beers.add(beer1);
        beers.add(beer2);
        beers.add(beer3);
        beers.add(beer4);
    }

    @Test
    void saveBeers() {
        beerRepository.saveBeers(beers);
        assertEquals("Share White",beerRepository.findByIdInBeers("sw-1"));
    }

    @Test
    void saveIngredients() {
        beerRepository.saveIngredients(beers.get(0));
//        beerRepository.saveBeers(beers);
//        System.out.println(beerRepository.findByIdInIngredients(1));
        assertEquals(0.002,beerRepository.findByIdInIngredients(1));
//        assertEquals();
    }

    @Test
    void filterBeersByBeerTypeDb() {

    }

    @Test
    void getIdsThatLackSpecificIngredientDb() {
    }

    @Test
    void getTheCheapestBrandDb() {
    }

    @Test
    void sortAllBeersByRemainingIngredientRatioDb() {
    }

    @Test
    void groupBeersByBrandDb() {
    }

    @Test
    void listBeersBasedOnTheirPriceWithATipDb() {
    }

    @Test
    void updatePriceTest(){
        beerRepository.saveBeers(beers);
        beerRepository.updatePrice();
        assertEquals(3600,beerRepository.findBeerByIdInBeers("lupa-1").getPrice());
//        EmptyResultDataAccessException emptyResultDataAccessException = assertThrows(EmptyResultDataAccessException.class,
//                ()->beerRepository.findBeerByIdInBeers("xyz"));
//        assertEquals("Incorrect result size: expected 1, actual 0",emptyResultDataAccessException.getMessage());

        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                ()->beerRepository.findBeerByIdInBeers("xyz"));
        assertEquals("No beer on this id: xyz",iae.getMessage());
    }

}