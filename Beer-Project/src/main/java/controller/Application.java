package controller;

import controller.enums.OutputFormat;
import controller.enums.Service;
import controller.validator.Validator;
import controller.validator.ValidatorImplementation;
import org.flywaydb.core.Flyway;
import org.mariadb.jdbc.MariaDbDataSource;
import repository.BeerRepoImplementation;
import service.BeerService;
import service.BeerServiceImplementation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Application {

    private static final String PATH = "src/main/resources/demo.json";
    private static final String PATH_PROPERTIES = "/beerstore.properties";
    Validator validator = new ValidatorImplementation();
    Scanner scanner = new Scanner(System.in);
    private OutputFormat outputFormat;
    private MariaDbDataSource dataSource;
    private BeerService beerService;

    public Application(MariaDbDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Application() {
    }

    public Application(BeerService beerService) {
        this.beerService = beerService;
    }

//    public static void main(String[] args) {
//        MariaDbDataSource dataSource = new MariaDbDataSource();
//
//        Properties prop = new Properties();
//        try (BufferedReader br = new BufferedReader(
//                new InputStreamReader(BeerRepoImplementation.class.getResourceAsStream(PATH_PROPERTIES)))) {
//            prop.load(br);
//            dataSource.setUrl(prop.getProperty("url"));
//            dataSource.setUser(prop.getProperty("user"));
//            dataSource.setPassword(prop.getProperty("password"));
//        } catch (IOException | SQLException ex) {
//            throw new IllegalStateException("Cannot reach file", ex);
//        }
//
//        Flyway fw = Flyway.configure().dataSource(dataSource).load();
//        fw.clean();
//        fw.migrate();
//
//
//        Application application = new Application(dataSource);
//        BeerService beerService = new BeerServiceImplementation(PATH);
//        application.openConsole(beerService);
//    }

    public void openConsole() {
        System.out.println("***********************\n   Beer Manager App\n***********************\n\nAdd your name:");
        String name;
        while (!validator.validateName(name = scanner.nextLine())) {
        }
        fillDatabaseOrNot(beerService);
        outputFormat = selectOutputFormat();
        selectService(beerService, name);
    }

    private void fillDatabaseOrNot(BeerService beerService) {
        System.out.println("Do you want to save data to DB?\n (Yes or No)");
        String option;
        while (!validator.validateFillingDataBaseInput(option = scanner.nextLine())) {
        }
        if (option.equalsIgnoreCase("yes")) {
            beerService.saveDataToDb();
        }
    }

    private OutputFormat selectOutputFormat() {
        System.out.println("\nSelect output format:\n1. Console\n2. Write to Json file");
        String line;
        int step;
        List<Integer> outputFormatNumbers = Arrays.stream(OutputFormat.values()).map(OutputFormat::getNumber).toList();
        while (!validator.validateNumbers(line = scanner.nextLine(), outputFormatNumbers)) {
        }
        step = Integer.parseInt(line);
        switch (step) {
            case 1:
                return OutputFormat.CONSOLE;
            case 2:
                return OutputFormat.JSON_FILE;
        }
        throw new IllegalStateException("Not ok input");
    }

    private void selectService(BeerService beerService, String name) {
        int step;
        String line;
        do {
            serviceMessage();
            List<Integer> services = Arrays.stream(Service.values()).map(Service::getNumber).toList();
            while (!validator.validateNumbers(line = scanner.nextLine(), services)) {
            }
            step = Integer.parseInt(line);
            switch (step) {
                case 1:
                    beerService.groupBeersByBrand(outputFormat, name);
                    break;
                case 2:
                    String type = askForType();
                    beerService.filterBeersByBeerType(type, outputFormat, name);
                    break;
                case 3:
                    beerService.getTheCheapestBrand(outputFormat, name);
                    break;
                case 4:
                    String ingredient = askForIngredient();
                    beerService.getIdsThatLackSpecificIngredient(ingredient, outputFormat, name);
                    break;
                case 5:
                    beerService.sortAllBeersByRemainingIngredientRatio(outputFormat, name);
                    break;
                case 6:
                    beerService.listBeersBasedOnTheirPriceWithATip(outputFormat, name);
                    break;
                case 7:
                    beerService.updatePrice();
                    break;
                case 8:
                    String beerId = askForBeerId();
                    isBeerDeleted(beerService.deleteBeerById(beerId));
                    break;
                case 9:
                    outputFormat = selectOutputFormat();
            }
        } while (step != 0);
    }

    private void isBeerDeleted(boolean deleteBeerById) {
        if (deleteBeerById) {
            System.out.println("Beer is deleted");
        } else {
            System.out.println("No beer with that id");
        }
    }

    private String askForBeerId() {
        System.out.println("Please add a beer id.");
        String line;
        while (!validator.validateInput(line = scanner.nextLine())) {
        }
        return line;
    }

    private void serviceMessage() {
        System.out.println(new StringBuilder()
                .append("\nSelect from options:\n")
                .append("1. Group beers by brand\n")
                .append("2. Filter beers by type\n")
                .append("3. Get the cheapest brand\n")
                .append("4. Get ids that lack from specific ingredient\n")
                .append("5. Sort all beers by remaining ingredient ratio\n")
                .append("6. List beers based on their price with a tip\n")
                .append("7. Round all price to hundred\n")
                .append("8. Delete beer by beer id\n")
                .append("9. Back\n")
                .append("0. Exit\n")
                .toString());
    }

    private String askForIngredient() {
        System.out.println(new StringBuilder()
                .append("\nSelect one ingredient:\n")
                .append("barley\n").append("corn\n")
                .append("salt\n").append("sugar\n")
                .append("wheat\n").toString());
        String line;
        while (!validator.validateIngredient(line = scanner.nextLine().toLowerCase())) {
        }
        return line;
    }

    private String askForType() {
        System.out.println(new StringBuilder()
                .append("\nSelect one type:\n")
                .append("Brown\n").append("Corn\n")
                .append("Pale Ale\n").append("Wheat\n")
                .append("White\n").toString());
        String line;
        while (!validator.validateType(line = scanner.nextLine().toLowerCase())) {
        }
        return line;
    }

    public void writeServiceResultToConsole(String result) {
        System.out.println(result);
    }
}
