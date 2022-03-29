package configuration;

import controller.Application;
import controller.validator.Validator;
import controller.validator.ValidatorImplementation;
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
    private static final String PATH_PROPERTIES = "/beerstore.properties";

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
        FileService fileService = new FileServiceImplementation();
        Validator validator = new ValidatorImplementation();
        Application application = new Application();
        BeerService beerService = new BeerServiceImplementation(beerRepo, fileService,application);
        Application application2 = new Application(beerService, validator);

        application2.openConsole();
    }
}
