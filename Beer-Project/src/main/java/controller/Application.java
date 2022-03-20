package controller;

import service.BeerManager;
import service.BeerManagerImplementation;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Application {

    public static final String PATH = "src/main/resources/demo.json";
    public static final List<String> TYPE_FILTER = Arrays.asList("Brown","Corn","Pale Ale","Wheat","White");
    public static final List<String> INGREDIENT_FILTER = Arrays.asList("barley","corn","salt","sugar","wheat");
    Scanner scanner = new Scanner(System.in);
    private int outputFormat;
    //    private String name;
    private static final String OUTPUT_FORMAT_NUMBERS = "123";
    private static final String SERVICE_FORMAT_NUMBERS = "12345678";

    public static void main(String[] args) {
        Application application = new Application();
        BeerManager beerManager = new BeerManagerImplementation(PATH);
        application.openConsole(beerManager);
    }

    public void openConsole(BeerManager beerManager) {
        System.out.println("***********************\n   Beer Manager App\n***********************\n\nAdd your name:");
        String name;
        while (validateName(name = scanner.nextLine())) {
        }
        selectOutputFormat();
        selectService(beerManager);
    }

    private void selectService(BeerManager beerManager) {
        int step;
        String line;
        do {
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
            while (validateNumbers(line = scanner.nextLine(), SERVICE_FORMAT_NUMBERS)) {
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

    private void selectOutputFormat() {
        System.out.println("\nSelect output format:\n1. Console\n2. Write to Json file\n3. Write to database");
        String line;
        int step;
        while (validateNumbers(line = scanner.nextLine(), OUTPUT_FORMAT_NUMBERS)) {
        }
        step = Integer.parseInt(line);
        switch (step) {
            case 1:
                outputFormat = 1;
                break;
            case 2:
                outputFormat = 2;
                break;
            case 3:
                outputFormat = 3;
                break;
            default:
                throw new IllegalArgumentException("Not ok input format: " + step);
        }
    }

    private String askForIngredient() {
        System.out.println(new StringBuilder()
                .append("\nSelect one ingredient:\n")
                .append("barley\n").append("corn\n")
                .append("salt\n").append("sugar\n")
                .append("wheat\n").toString());
        return askNewInputIfPrevWasWrong();
    }

    private String askNewInputIfPrevWasWrong() {
        String line;
        do {
            line = scanner.nextLine().toLowerCase();
        } while (validateName(line));
        return line;
    }

    private String askForType() {
        System.out.println(new StringBuilder()
                .append("\nSelect one type:\n")
                .append("Brown\n").append("Corn\n")
                .append("Pale Ale\n").append("Wheat\n")
                .append("White\n").toString());
        return scanner.nextLine().toLowerCase();
    }

    private int validateChoseLine(String nextLine) {
        if (nextLine == null || nextLine.trim().equals("")) {
            return 1;
        }
        for (Character c : nextLine.trim().toCharArray()) {
            if (!Character.isDigit(c)) {
                System.out.println("Given parameter is not number");
            }
        }
        return Integer.parseInt(nextLine);
    }

    private boolean validateName(String nextLine) {
        if (nextLine == null || nextLine.trim().equals("")) {
            System.out.println("Name not correct.\nPlease add your name!");
            return true;
        } else {
            return false;
        }
    }

    private boolean validateNumbers(String nextLine, String numbers) {
        String line;
        if (nextLine == null || nextLine.trim().equals("")) {
            System.out.println("Given parameter is not ok");
            return true;
        }
        line = nextLine.trim();
        if (line.length() != 1) {
            System.out.println("Given parameter is not ok");
            return true;
        } else if (!Character.isDigit(line.charAt(0))) {
            System.out.println("Given parameter is not a number");
            return true;
        } else if (numbers.contains(line)) {
            return false;
        }
        System.out.println("Given parameter is not ok");
        return true;
    }
}
