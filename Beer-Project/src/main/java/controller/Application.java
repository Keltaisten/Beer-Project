package controller;

import controller.enums.OutputFormat;
import controller.enums.Service;
import controller.validator.ValidatorImplementation;
import service.BeerService;
import service.BeerServiceImplementation;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static final String PATH = "src/main/resources/demo.json";
    ValidatorImplementation validator = new ValidatorImplementation();
    Scanner scanner = new Scanner(System.in);
//    private static String name;

    public static void main(String[] args) {
        Application application = new Application();
        BeerService beerService = new BeerServiceImplementation(PATH);
        application.openConsole(beerService);
    }

    public void openConsole(BeerService beerService) {
        System.out.println("***********************\n   Beer Manager App\n***********************\n\nAdd your name:");
        String name;
        while (!validator.validateName(name = scanner.nextLine())) {
        }
        fillDatabaseOrNot(beerService);
        OutputFormat output = selectOutputFormat();
        selectService(beerService, output, name);
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
//                outputFormat = 1;
                return OutputFormat.CONSOLE;
            case 2:
//                outputFormat = 2;
                return OutputFormat.JSON_FILE;
        }
        throw new IllegalStateException("Not ok input");
    }

    private void selectService(BeerService beerManager, OutputFormat outputFormat, String name) {
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
                    beerManager.groupBeersByBrand(outputFormat, name);
                    break;
                case 2:
                    String type = askForType();
                    beerManager.filterBeersByBeerType(type, outputFormat, name);
                    break;
                case 3:
                    beerManager.getTheCheapestBrand(outputFormat, name);
                    break;
                case 4:
                    String ingredient = askForIngredient();
                    beerManager.getIdsThatLackSpecificIngredient(ingredient, outputFormat, name);
                    break;
                case 5:
                    beerManager.sortAllBeersByRemainingIngredientRatio(outputFormat, name);
                    break;
                case 6:
                    beerManager.listBeersBasedOnTheirPriceWithATip(outputFormat, name);
                    break;
                case 7:
                    beerManager.updatePrice();
                    break;
                case 8:
                    String beerId = askForBeerId();
                    isBeerDeleted(beerManager.deleteBeerById(beerId));
                    break;
                case 9:
                    selectOutputFormat();
            }
        } while (step != 0);
    }

    private void isBeerDeleted(boolean deleteBeerById) {
        if(deleteBeerById){
            System.out.println("Beer is deleted");
        }
        else {
            System.out.println("No beer with that id");
        }
    }

    private String askForBeerId() {
        System.out.println("Please add a beer id.");
        String line;
        while (!validator.validateInput(line = scanner.nextLine())){
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
}
