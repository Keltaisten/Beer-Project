package service;

import beercatalog.Beer;
import beercatalog.BrandsWithBeers;
import beercatalog.Ingredient;
import controller.OutputFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.BeerRepoImplementation;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class BeerManagerImplementationTest {
    BeerServiceImplementation beerManager = new BeerServiceImplementation();

    @BeforeEach
    void init() {
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
        beerManager.addBeer(beer1);
        beerManager.addBeer(beer2);
        beerManager.addBeer(beer3);
        beerManager.addBeer(beer4);
        BrandsWithBeers brandsWithBeers1 = new BrandsWithBeers("Coding Challenge Brewery", Arrays.asList("ccw-1"));
        BrandsWithBeers brandsWithBeers2 = new BrandsWithBeers("Level Up Brewery", Arrays.asList("lupa-1"));
        BrandsWithBeers brandsWithBeers3 = new BrandsWithBeers("Share", Arrays.asList("sw-1", "spa-1"));
        beerManager.addBrandsWithBeers(brandsWithBeers1);
        beerManager.addBrandsWithBeers(brandsWithBeers2);
        beerManager.addBrandsWithBeers(brandsWithBeers3);
        BeerRepoImplementation beerRepo = new BeerRepoImplementation();
        beerRepo.saveBeers(beerManager.getBeers());
    }

    @Test
    void groupBeersByBrand() {
        assertEquals(2, beerManager.groupBeersByBrand(null).get(2).getBeers().size());
        assertEquals("Share", beerManager.groupBeersByBrand(null).get(2).getBrand());
        assertEquals("Coding Challenge Brewery", beerManager.groupBeersByBrand(null)
                .get(0).getBrand());
        assertEquals(beerManager.getBrandsWithBeers(), beerManager.groupBeersByBrand(null));

    }

    @Test
    void filterBeersByBeerType() {
        assertEquals(Arrays.asList("ccw-1", "sw-1"), beerManager.filterBeersByBeerType("white", null));
        assertEquals(0, beerManager.filterBeersByBeerType("brown", null).size());
    }

    @Test
    void getTheCheapestBrandTest() {
        assertEquals("Level Up Brewery", beerManager.getTheCheapestBrand(null));
    }

    @Test
    void getTheCheapestBrandWithEmptyListTest() {
        beerManager.getBeers().clear();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> beerManager.getTheCheapestBrand(null));
        assertEquals("No data in the list", exception.getMessage());
    }

    @Test
    void getIdsThatLackSpecificIngredient() {
        assertEquals(Arrays.asList("lupa-1", "spa-1"), beerManager.getIdsThatLackSpecificIngredient("wheat", null));
        assertEquals(0, beerManager.getIdsThatLackSpecificIngredient("sugar", null).size());
    }

    @Test
    void sortAllBeersByRemainingIngredientRatio() {
        List<String> beers = Arrays.asList("ccw-1", "lupa-1", "spa-1", "sw-1");
        assertEquals(beers, beerManager.sortAllBeersByRemainingIngredientRatio(null));
    }

    @Test
    void listBeersBasedOnTheirPriceWithATip() {
        assertEquals(4700, beerManager.listBeersBasedOnTheirPriceWithATip(null).keySet().toArray()[1]);
        assertEquals(Arrays.asList("sw-1"), beerManager.listBeersBasedOnTheirPriceWithATip(null).get(6500));
    }

    @Test
    void convertToJsonWriteToFileExceptionTest() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> beerManager.convertToJson("word",2, OutputFormat.JSON_FILE,"\"/:?*!<>|"));
        assertEquals("Cannot write file", exception.getMessage());
    }
}