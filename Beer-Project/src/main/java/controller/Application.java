package controller;

import beercatalog.Beer;
import service.BeerService;
import service.BeerServiceImplementation;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static final String PATH = "src/main/resources/demo.json";
    public static final List<String> TYPES = Arrays.asList("brown","corn","pale ale","wheat","white");
    public static final List<String> INGREDIENTS = Arrays.asList("barley","corn","salt","sugar","wheat");
    private static final String OUTPUT_FORMAT_NUMBERS = "12";
    private static final String SERVICE_FORMAT_NUMBERS = "12345678";

    ValidatorImplementation validator = new ValidatorImplementation();
    Scanner scanner = new Scanner(System.in);
    private int outputFormat;
    //    private String name;

    public static void main(String[] args) {
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
        selectOutputFormat();
        selectService(beerService);
    }

    private void fillDatabaseOrNot(BeerService beerService) {
        System.out.println("Do you want to save data to DB?\n (Yes or No)");
        String option;
        while (validator.validateFillingDataBaseInput(option = scanner.nextLine())) {
        }
        if(option.equalsIgnoreCase("yes")){
            beerService.saveDataToDb();
        }
    }

    private void selectOutputFormat() {
        System.out.println("\nSelect output format:\n1. Console\n2. Write to Json file\n3. Write to database");
        String line;
        int step;
        while (validator.validateNumbers(line = scanner.nextLine(), OUTPUT_FORMAT_NUMBERS)) {
        }
        step = Integer.parseInt(line);
        switch (step) {
            case 1:
                outputFormat = 1;
                break;
            case 2:
                outputFormat = 2;
        }
    }

    private void selectService(BeerService beerManager) {
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
        while (validator.validateInput(line = scanner.nextLine().toLowerCase(),INGREDIENTS)){
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
        while (validator.validateInput(line = scanner.nextLine().toLowerCase(),TYPES)){
        }
        return line;
    }
}
