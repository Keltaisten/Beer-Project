package repository;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class BeerRepositoryTest {

    BeerRepository beerRepository = new BeerRepository();

    @BeforeEach
    void initTest() {
        Properties prop = new Properties();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(BeerRepository.class.getResourceAsStream("/beerstore.properties")))) {
            prop.load(br);
//            System.out.println(prop.getProperty("url"));
//            System.out.println(prop.getProperty("user"));
//            System.out.println(prop.getProperty("password"));
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl(prop.getProperty("url"));
            dataSource.setUser(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));
            beerRepository.init();
//            return dataSource;
            Flyway fw = Flyway.configure().dataSource(dataSource).load();
            fw.clean();
            fw.migrate();
        } catch (IOException | SQLException ex) {
            throw new IllegalStateException("Cannot reach file", ex);
        }

    }

    @Test
    void separateTest() {

    }

    @Test
    void getTheCheapestBrandToDbTest(){
//        beerRepository.getTheCheapestBrandToDb("cd01","the_cheapest_brand");
        assertEquals("cd01",beerRepository.insertThanFindById("cd01"));
    }

    @Test
    void insertBeerIdWithKeyThanSelectIt(){
        long id = beerRepository.saveBeerIdAndGetGeneratedKey("csc02");
        String beerId = beerRepository.findById(id);
        assertEquals("csc02",beerId);
    }

    @Test
    void listsToDbTest() {
    }

    @Test
    void filterBeersByBeerTypeToDbTest() {

    }

    @Test
    void groupBeersByBrandToDb() {
    }
}