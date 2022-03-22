package controller;

import beercatalog.Beer;
import service.BeerService;
import service.BeerServiceImplementation;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Application {

    public static final String PATH = "src/main/resources/demo.json";
    private static final String SERVICE_FORMAT_NUMBERS = "12345678";
//    private OutputFormat outputFormate;
    ValidatorImplementation validator = new ValidatorImplementation();
    Scanner scanner = new Scanner(System.in);
//    private int outputFormat;
    //    private String name;

    public static void main(String[] args) {
//        System.out.println(OutputFormat.values().toString());
//        System.out.println(Arrays.stream(OutputFormat.values()).map(OutputFormat::getNumber).collect(Collectors.toList()));
        Application application = new Application();
        BeerService beerManager = new BeerServiceImplementation(PATH);
        application.openConsole(beerManager);
    }

    public void openConsole(BeerService beerService) {
        System.out.println("***********************\n   Beer Manager App\n***********************\n\nAdd your name:");
        String name;
        while (validator.validateName(name = scanner.nextLine())) {
        }
        fillDatabaseOrNot(beerService);
        OutputFormat output = selectOutputFormat();
        selectService(beerService, output);
    }

    private void fillDatabaseOrNot(BeerService beerService) {
        System.out.println("Do you want to save data to DB?\n (Yes or No)");
        String option;
        while (validator.validateFillingDataBaseInput(option = scanner.nextLine())) {
        }
        if (option.equalsIgnoreCase("yes")) {
            beerService.saveDataToDb();
        }
    }

    private OutputFormat selectOutputFormat() {
        System.out.println("\nSelect output format:\n1. Console\n2. Write to Json file\n3. Write to database");
        String line;
        int step;
        String outputFormatNumbers = Arrays.stream(OutputFormat.values()).map(OutputFormat::getNumber).toString();
        while (validator.validateNumbers(line = scanner.nextLine(), outputFormatNumbers)) {
        }
        step = Integer.parseInt(line);
        switch (step) {
            case 1:
//                outputFormat = 1;
                return OutputFormat.CONSOLE;
            case 2:
//                outputFormat = 2;
                return OutputFormat.JSON_FILE;
        }
        throw new IllegalStateException("Not ok input");
    }

    private void selectService(BeerService beerManager, OutputFormat outputFormat) {
        int step;
        String line;
        do {
            serviceMessage();
            while (validator.validateNumbers(line = scanner.nextLine(), SERVICE_FORMAT_NUMBERS)) {
            }
            step = Integer.parseInt(line);
            switch (step) {
                case 1:
                    beerManager.groupBeersByBrand(outputFormat);
                    break;
                case 2:
                    String type = askForType();
                    beerManager.filterBeersByBeerType(type, outputFormat);
                    break;
                case 3:
                    beerManager.getTheCheapestBrand(outputFormat);
                    break;
                case 4:
                    String ingredient = askForIngredient();
                    beerManager.getIdsThatLackSpecificIngredient(ingredient, outputFormat);
                    break;
                case 5:
                    beerManager.sortAllBeersByRemainingIngredientRatio(outputFormat);
                    break;
                case 6:
                    beerManager.listBeersBasedOnTheirPriceWithATip(outputFormat);
                    break;
                case 7:
                    selectOutputFormat();
            }
        } while (step != 8);
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
                .append("7. Back\n")
                .append("8. Exit\n")
                .toString());
    }

    private String askForIngredient() {
        System.out.println(new StringBuilder()
                .append("\nSelect one ingredient:\n")
                .append("barley\n").append("corn\n")
                .append("salt\n").append("sugar\n")
                .append("wheat\n").toString());
        String line;
        while (validator.validateIngredient(line = scanner.nextLine().toLowerCase())) {
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
        while (validator.validateType(line = scanner.nextLine().toLowerCase())) {
        }
        return line;
    }
}
