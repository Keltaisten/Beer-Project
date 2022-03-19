package repository;

import beercatalog.BrandsWithBeers;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class BeerRepository {
    private MariaDbDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

//    public BeerRepository(MariaDbDataSource dataSource) {
//        this.dataSource = dataSource;
//        jdbcTemplate = new JdbcTemplate(dataSource);
//    }


    public BeerRepository() {
        this.dataSource = init();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public MariaDbDataSource init() {
//        BeerRepository beerRepository;
//        try {
//            MariaDbDataSource dataSource = new MariaDbDataSource();
//            dataSource.setUrl("jdbc:mariadb://localhost:3306/people?useUnicode=true");
//            dataSource.setUser("peopleuser");
//            dataSource.setPassword("peoplepassword");
////            beerRepository = new BeerRepository(dataSource);


            Properties prop = new Properties();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(BeerRepository.class.getResourceAsStream("/beerstore.properties")))){
            prop.load(br);
//            System.out.println(prop.getProperty("url"));
//            System.out.println(prop.getProperty("user"));
//            System.out.println(prop.getProperty("password"));
            MariaDbDataSource dataSource = new MariaDbDataSource();
            dataSource.setUrl(prop.getProperty("url"));
            dataSource.setUser(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));
            return dataSource;
        }catch (IOException | SQLException ex){
            throw new IllegalStateException("Cannot reach file", ex);
        }


//        } catch (SQLException sqle) {
//            throw new IllegalStateException("Can not reach database.", sqle);
//        }
    }

    public void separate(Object o, int task, String nameOfTheTask) {
        if (task == 3) {
            getTheCheapestBrandToDb(o, nameOfTheTask);
        } else if (task == 2 || task == 4 || task == 5) {
            filterBeersByBeerTypeToDb(o, nameOfTheTask);
        } else if (task == 1) {
            groupBeersByBrandToDb(o,nameOfTheTask);
        } else if (task == 6) {

        }
    }

    public void getTheCheapestBrandToDb(Object o, String nameOfTask) {
        String sqlWord = "insert into " + nameOfTask + " (name_of_brand) values (?);";
        jdbcTemplate.update(sqlWord, o);
    }
//        jdbcTemplate.update("insert into the_cheapest_brand (name_of_brand) values (?);", o);

    public void filterBeersByBeerTypeToDb(Object o, String nameOfTask) {
        List<String> beerIds = (List<String>) o;
        for (String s : beerIds) {
            jdbcTemplate.update("insert into filter_beers_by_type (ids_of_beers) values (?);", s
            );
        }
    }

    public void groupBeersByBrandToDb(Object o, String nameOfTask) {
        List<BrandsWithBeers> brandsWithBeers = (List<BrandsWithBeers>) o;
        for (BrandsWithBeers bwb : brandsWithBeers) {
            for (String s : bwb.getBeers()) {
                jdbcTemplate.update("insert into group_beers_by_brand (brand_of_beers, ids_of_beers) values (?, ?);", bwb.getBrand(), s);
            }
        }
    }
}
