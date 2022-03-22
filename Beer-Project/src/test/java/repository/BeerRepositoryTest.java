package repository;

import beercatalog.BrandsWithBeers;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class BeerRepositoryTest {

    BeerRepositoryOld beerRepository = new BeerRepositoryOld();

    @BeforeEach
    void initTest() {
        Properties prop = new Properties();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(BeerRepositoryOld.class.getResourceAsStream("/beerstore.properties")))) {
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
    }

    @Test
    void getTheCheapestBrandToDbTest() {
//        beerRepository.getTheCheapestBrandToDb("cd01","the_cheapest_brand");
//        assertEquals("cd01", beerRepository.insertThanFindByIdAndGetBeerName("cd01"));
    }

    @Test
    void insertBeerIdWithKeyThanSelectIt() {
        long id = beerRepository.saveBeerIdAndGetGeneratedKey("csc02");
        String beerId = beerRepository.findById(id);
        assertEquals("csc02", beerId);
    }

    @Test
    void listsToDbTest() {
        List<String> temp = Arrays.asList("bsc-1", "ccpa-1", "bspa-1", "sw-1");
        beerRepository.listsToDb(temp,
                "sort_all_beers_by_remaining_ingredient_ratio");
        assertEquals(temp, beerRepository.selectBeerIdData("sort_all_beers_by_remaining_ingredient_ratio", "ids_of_beers").get());
    }

    //    @Test
//    void filterBeersByBeerTypeToDbTest() {
//
//    }
//
    @Test
    void groupBeersByBrandToDbTest() {
        List<BrandsWithBeers> brandsWithBeers = new ArrayList<>();
        brandsWithBeers.add(new BrandsWithBeers("Coding Challenge Brewery", Arrays.asList("ccw-1")));
        brandsWithBeers.add(new BrandsWithBeers("Level Up Brewery", Arrays.asList("lupa-1")));
        brandsWithBeers.add(new BrandsWithBeers("Share", Arrays.asList("sw-1", "spa-1")));
        beerRepository.groupBeersByBrandToDb(brandsWithBeers, "group_beers_by_brand");
        List<String> actual = beerRepository.selectBeerIdData("group_beers_by_brand",
                "brand_of_beers").get().stream().distinct().collect(Collectors.toList());
        assertEquals(Arrays.asList("Coding Challenge Brewery", "Level Up Brewery", "Share"), actual);
//        assertEquals(Arrays.asList("Coding Challenge Brewery", "Level Up Brewery", "Share"),
//                beerRepository.selectBeerData("group_beers_by_brand", "brand_of_beers").get());
    }

    @Test
    void listBeersBasedOnTheirPriceWithATipToDbTest() {
        Map<Integer, List<String>> temp = new TreeMap<>();
        temp.put(1000, Arrays.asList("bsc-1"));
        temp.put(5500, Arrays.asList("sw-2"));
        temp.put(9800, Arrays.asList("spa-1", "lub-1"));
        beerRepository.listBeersBasedOnTheirPriceWithATipToDb(temp,
                "list_beers_based_on_their_price_with_a_tip");
        List<Integer> actual = beerRepository.selectBeerTipData("list_beers_based_on_their_price_with_a_tip",
                "rounded_price").get().stream().distinct().collect(Collectors.toList());
        assertEquals(3, actual.size());
        assertEquals(Arrays.asList(1000, 5500, 9800), actual);
    }
}