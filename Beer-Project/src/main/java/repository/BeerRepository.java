package repository;

import beercatalog.BrandsWithBeers;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class BeerRepository {
    private MariaDbDataSource dataSource;
    private JdbcTemplate jdbcTemplate;

    public BeerRepository() {
        this.dataSource = init();
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public MariaDbDataSource init() {
        Properties prop = new Properties();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(BeerRepository.class.getResourceAsStream("/beerstore.properties")))) {
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

    public void separate(Object o, int task, String nameOfTheTask) {
        if (task == 3) {
            getTheCheapestBrandToDb(o, nameOfTheTask);
        } else if (task == 2 || task == 4 || task == 5) {
            listsToDb(o, nameOfTheTask);
        } else if (task == 1) {
            groupBeersByBrandToDb(o, nameOfTheTask);
        } else if (task == 6) {
            listBeersBasedOnTheirPriceWithATipToDb(o, nameOfTheTask);
        }
    }

    public void listBeersBasedOnTheirPriceWithATipToDb(Object o, String nameOfTheTask) {
        Map<Integer, List<String>> beersAndRoundedPrices = (Map<Integer, List<String>>) o;
        for (Map.Entry entry : beersAndRoundedPrices.entrySet()) {
            for (String s : (List<String>) entry.getValue()) {
                jdbcTemplate.update("insert into list_beers_based_on_their_price_with_a_tip " +
                        "(rounded_price, ids_of_beers) values (?, ?);", entry.getKey(), s);
            }
        }
    }

    public void getTheCheapestBrandToDb(Object o, String nameOfTask) {
        jdbcTemplate.update("insert into the_cheapest_brand (name_of_brand) values (?);", o);
//        String sqlWord = "insert into " + nameOfTask + " (name_of_brand) values (?);";
//        jdbcTemplate.update(sqlWord, o);
    }
//        jdbcTemplate.update("insert into the_cheapest_brand (name_of_brand) values (?);", o);

    public void listsToDb(Object o, String nameOfTask) {

        List<String> beerIds = (List<String>) o;
        for (String s : beerIds) {
            String sqlWord = "insert into " + nameOfTask + " (ids_of_beers) values (?);";
            jdbcTemplate.update(sqlWord, s);
//            jdbcTemplate.update("insert into filter_beers_by_type (ids_of_beers) values (?);", s);
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

    public String insertThanFindByIdAndGetBeerName(String beer) {
        long id = -1;
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "insert into the_cheapest_brand (name_of_brand) values (?);",
                     Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, beer);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys();) {
                if (rs.next()) {
                    id = rs.getLong(1);
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not insert.", sqle);
        }
        return jdbcTemplate.queryForObject(
                "select * from the_cheapest_brand where id = ?;",
                (rs, rowNum) ->
                        rs.getString("name_of_brand"),
                id);
    }

    public String findById(long id) {
        return jdbcTemplate.queryForObject(
                "select * from the_cheapest_brand where id = ?;",
                (rs, rowNum) ->
                        rs.getString("name_of_brand"),
                id);
    }

    public long saveBeerIdAndGetGeneratedKey(String beer) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "insert into the_cheapest_brand (name_of_brand) values (?);",
                     Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, beer);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys();) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        } catch (SQLException sqle) {
            throw new IllegalStateException("Can not insert.", sqle);
        }
        throw new IllegalArgumentException("Can not generate id");
    }

    public Optional<List<String>> selectBeerIdData(String nameOfTable, String nameOfColumn){
        String column = "distinct " + nameOfColumn;
        String sqlTemp = "select distinct * from " + nameOfTable + ";";
        return Optional.of(jdbcTemplate.query(sqlTemp,
                (rs, rowNum) -> rs.getString(nameOfColumn)));
    }

    public Optional<List<Integer>> selectBeerTipData(String nameOfTable, String nameOfColumn){
        String column = "distinct " + nameOfColumn;
        String sqlTemp = "select distinct * from " + nameOfTable + ";";
        return Optional.of(jdbcTemplate.query(sqlTemp,
                (rs, rowNum) -> rs.getInt(nameOfColumn)));
    }

//    public Optional<List<String>> selectBeerIds(String nameOfTable){
//        String sqlTemp = "select * from " + nameOfTable + ";";
//        return Optional.of(jdbcTemplate.query(sqlTemp,
//                (rs, rowNum) -> rs.getString("ids_of_beers")));
//    }

}
