package configuration;

import controller.Application;
import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import repository.BeerRepo;
import repository.BeerRepoImplementation;
import service.BeerService;
import service.BeerServiceImplementation;
import service.FileService;
import service.FileServiceImplementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Properties;

public class Main {
    private MariaDbDataSource dataSource;
    private static final String PATH_PROPERTIES = "/beerstore.properties";
    private static final String PATH = "src/main/resources/demo.json";

    public static void main(String[] args) {
        MariaDbDataSource dataSource = new MariaDbDataSource();

        Properties prop = new Properties();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(BeerRepoImplementation.class.getResourceAsStream(PATH_PROPERTIES)))) {
            prop.load(br);
            dataSource.setUrl(prop.getProperty("url"));
            dataSource.setUser(prop.getProperty("user"));
            dataSource.setPassword(prop.getProperty("password"));
        } catch (IOException | SQLException ex) {
            throw new IllegalStateException("Cannot reach file", ex);
        }

        Flyway fw = Flyway.configure().dataSource(dataSource).load();
        fw.clean();
        fw.migrate();


        BeerRepo beerRepo = new BeerRepoImplementation(dataSource);
        FileService fileService = new FileServiceImplementation(beerRepo);
        BeerService beerService = new BeerServiceImplementation(beerRepo, fileService);
//        FileService fileService = new FileServiceImplementation(beerService);
        Application application = new Application(beerService);

        application.openConsole();
    }
}
