package beercatalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.BeerServiceImplementation;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class BrandsWithBeersTest {
    BeerServiceImplementation beerServiceImpl = new BeerServiceImplementation();

    @BeforeEach
    void init(){
        BrandsWithBeers brandsWithBeers1 = new BrandsWithBeers("Coding Challenge Brewery", Arrays.asList("ccw-1"));
        BrandsWithBeers brandsWithBeers2 = new BrandsWithBeers("Level Up Brewery", Arrays.asList("lupa-1"));
        BrandsWithBeers brandsWithBeers3 = new BrandsWithBeers("Share", Arrays.asList("sw-1", "spa-1"));
        beerServiceImpl.addBrandsWithBeers(brandsWithBeers1);
        beerServiceImpl.addBrandsWithBeers(brandsWithBeers2);
        beerServiceImpl.addBrandsWithBeers(brandsWithBeers3);
    }

    @Test
    void getBrand() {
        assertEquals(3,beerServiceImpl.getBrandsWithBeers().size());
        assertEquals("Share",beerServiceImpl.getBrandsWithBeers().get(2).getBrand());
    }

    @Test
    void getBeers() {
        assertEquals(Arrays.asList("sw-1","spa-1"),beerServiceImpl.getBrandsWithBeers().get(2).getBeers());
    }
}