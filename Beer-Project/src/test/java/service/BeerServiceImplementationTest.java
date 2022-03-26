package service;

import beercatalog.Beer;
import beercatalog.BrandsWithBeers;
import beercatalog.Ingredient;
import controller.enums.OutputFormat;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;
import repository.BeerRepoImplementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BeerServiceImplementationTest {
    BeerServiceImplementation beerServiceImpl = new BeerServiceImplementation();
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
        beerServiceImpl.addBeer(beer1);
        beerServiceImpl.addBeer(beer2);
        beerServiceImpl.addBeer(beer3);
        beerServiceImpl.addBeer(beer4);
        BrandsWithBeers brandsWithBeers1 = new BrandsWithBeers("Coding Challenge Brewery", Arrays.asList("ccw-1"));
        BrandsWithBeers brandsWithBeers2 = new BrandsWithBeers("Level Up Brewery", Arrays.asList("lupa-1"));
        BrandsWithBeers brandsWithBeers3 = new BrandsWithBeers("Share", Arrays.asList("sw-1", "spa-1"));
        beerServiceImpl.addBrandsWithBeers(brandsWithBeers1);
        beerServiceImpl.addBrandsWithBeers(brandsWithBeers2);
        beerServiceImpl.addBrandsWithBeers(brandsWithBeers3);
//        beerServiceImpl.getBeerRepo().saveBeers(beerServiceImpl.getBeers());

//        BeerRepoImplementation beerRepo = new BeerRepoImplementation();
//        beerRepo.saveBeers(beerManager.getBeers());
    }

    @Test
    void groupBeersByBrand() {
//        beerRepository.saveBeers();
        beerServiceImpl.getBeerRepo().saveBeers(beerServiceImpl.getBeers());
        assertEquals(2, beerServiceImpl.groupBeersByBrand(OutputFormat.CONSOLE, "John").get(4).getBeers().size());
        assertEquals("Share", beerServiceImpl.groupBeersByBrand(OutputFormat.CONSOLE, "John").get(4).getBrand());
        assertEquals("Coding Challenge Brewery", beerServiceImpl.groupBeersByBrand(OutputFormat.CONSOLE, "John")
                .get(0).getBrand());
    }

    @Test
    void filterBeersByBeerType() {
        beerServiceImpl.getBeerRepo().saveBeers(beerServiceImpl.getBeers());
        assertEquals(Arrays.asList("ccw-1", "sw-1"), beerServiceImpl.filterBeersByBeerType("white", OutputFormat.CONSOLE, "John"));
        assertEquals(0, beerServiceImpl.filterBeersByBeerType("brown", OutputFormat.CONSOLE, "John").size());
    }

    @Test
    void getTheCheapestBrandTest() {
        beerServiceImpl.getBeerRepo().saveBeers(beerServiceImpl.getBeers());
        assertEquals("Level Up Brewery", beerServiceImpl.getTheCheapestBrand(OutputFormat.CONSOLE, "John"));
    }

    @Test
    void getTheCheapestBrandWithEmptyListTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> beerServiceImpl.getTheCheapestBrand(OutputFormat.CONSOLE, "John"));
        assertEquals("No data in the list", exception.getMessage());
    }

    @Test
    void getIdsThatLackSpecificIngredient() {
        beerServiceImpl.getBeerRepo().saveBeers(beerServiceImpl.getBeers());
        assertEquals(Arrays.asList("lupa-1", "spa-1"), beerServiceImpl.getIdsThatLackSpecificIngredient("wheat", OutputFormat.CONSOLE, "John"));
        assertEquals(0, beerServiceImpl.getIdsThatLackSpecificIngredient("sugar", OutputFormat.CONSOLE, "John").size());
    }

    @Test
    void sortAllBeersByRemainingIngredientRatio() {
        beerServiceImpl.getBeerRepo().saveBeers(beerServiceImpl.getBeers());
        List<String> beers = Arrays.asList("spa-1", "lupa-1", "sw-1", "ccw-1");
        assertEquals(beers, beerServiceImpl.sortAllBeersByRemainingIngredientRatio(OutputFormat.CONSOLE, "John"));
    }

    @Test
    void listBeersBasedOnTheirPriceWithATip() {
        beerServiceImpl.getBeerRepo().saveBeers(beerServiceImpl.getBeers());
        assertEquals(4700, beerServiceImpl.listBeersBasedOnTheirPriceWithATip(OutputFormat.CONSOLE, "John").keySet().toArray()[1]);
        assertEquals(Arrays.asList("sw-1"), beerServiceImpl.listBeersBasedOnTheirPriceWithATip(OutputFormat.CONSOLE, "John").get(6500));
    }

    @Test
    void convertToJsonWriteToFileExceptionTest() {
        beerServiceImpl.getBeerRepo().saveBeers(beerServiceImpl.getBeers());
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> beerServiceImpl.convertToJson("word", 2, OutputFormat.JSON_FILE, "\"/:?*!<>|", "John"));
        assertEquals("Cannot write file", exception.getMessage());
    }

    @Test
    void roundPriceTest() {
        assertEquals(1600, beerServiceImpl.roundPrice(1522));
        assertEquals(1700, beerServiceImpl.roundPrice(1700));
    }
}