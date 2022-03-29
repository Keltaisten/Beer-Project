package repository;

import beercatalog.Beer;
import beercatalog.Ingredient;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

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

    BeerRepo beerRepository;
    List<Beer> beers = new ArrayList<>();

    @BeforeEach
    void initTest() {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        Properties prop = new Properties();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(BeerRepoImplementation.class.getResourceAsStream("/beerstore.properties")))) {
            prop.load(br);
//            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl(prop.getProperty("url"));
            dataSource.setUser(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));
            beerRepository = new BeerRepoImplementation(dataSource);
//            beerRepository.init();
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
        Beer beer5 = new Beer("spa-1", "Beer Sans Corn", "Beer Sans Brewery",
                "Corn", 910, 0.129,
                Arrays.asList(
                        new Ingredient("salt", 0.004),
                        new Ingredient("sugar", 0.027),
                        new Ingredient("barley", 0),
                        new Ingredient("wheat", 0),
                        new Ingredient("corn", 0.221)));
        beers.add(beer1);
        beers.add(beer2);
        beers.add(beer3);
        beers.add(beer4);
        beers.add(beer5);
        beerRepository.saveBeers(beers);
    }

    @Test
    void saveBeers() {
        assertEquals("Share White",beerRepository.findBeerNameByIdInBeers("sw-1"));
    }

    @Test
    void saveIngredients() {
        beerRepository.saveIngredients(beers.get(0));
        assertEquals(0.002,beerRepository.findRatioByIdInIngredients(1));
    }

    @Test
    void filterBeersByBeerTypeDb() {
        assertEquals(2,beerRepository.filterBeersByBeerTypeDb("White").get().size());
    }

    @Test
    void getIdsThatLackSpecificIngredientDb() {
        assertEquals("spa-1",beerRepository.getIdsThatLackSpecificIngredientDb("barley").get(0));
    }

    @Test
    void getTheCheapestBrandDb() {
        assertEquals("Coding Challenge Brewery",beerRepository.getBeersAndPricesForTheCheapestBrandDb().get(0).getBeer());
        assertEquals(4645,beerRepository.getBeersAndPricesForTheCheapestBrandDb().get(0).getPrice());
    }

    @Test
    void sortAllBeersByRemainingIngredientRatioDb() {
        assertEquals(25,beerRepository.getBeerIdsWithIngrRatioForsortAllBeersByRemainingIngredientRatioDb().get().size());
        assertEquals("ccw-1",beerRepository.getBeerIdsWithIngrRatioForsortAllBeersByRemainingIngredientRatioDb().get().get(2).getId());
        assertEquals(0.025,beerRepository.getBeerIdsWithIngrRatioForsortAllBeersByRemainingIngredientRatioDb().get().get(2).getRatio());
    }

    @Test
    void groupBeersByBrandDb() {
        assertEquals(5,beerRepository.groupBeersByBrandDb().size());
        assertEquals("Share",beerRepository.groupBeersByBrandDb().get(4).getBrand());
        assertEquals(Arrays.asList("sw-1","spa-1"),beerRepository.groupBeersByBrandDb().get(4).getBeers());
    }

    @Test
    void listBeersBasedOnTheirPriceWithATipDb() {
        assertEquals(5,beerRepository.listBeersWithPriceForTipCalculationDb().get().size());
        assertEquals("ccw-1",beerRepository.listBeersWithPriceForTipCalculationDb().get().get(0).getBeer());
        assertEquals(4645,beerRepository.listBeersWithPriceForTipCalculationDb().get().get(0).getPrice());
    }

    @Test
    void updatePriceTest(){
        beerRepository.selectAndUpdatePrice();
        assertEquals(3600,beerRepository.findBeerByIdInBeers("lupa-1").getPrice());
        IllegalArgumentException iae = assertThrows(IllegalArgumentException.class,
                ()->beerRepository.findBeerByIdInBeers("xyz"));
        assertEquals("No beer on this id: xyz",iae.getMessage());
    }

    @Test
    void findByIdInIngredientsSumTest(){
        assertEquals(0.055,beerRepository.getRatioSumByIdInIngredients("ccw-1"),0.0001);
    }

    @Test
    void deleteBeerByIdDbTest(){
        assertTrue(beerRepository.deleteBeerByIdDb("sw-1"));
        assertFalse(beerRepository.deleteBeerByIdDb("hp-1"));
    }

}