package control;

import service.BeerManager;
import service.BeerManagerImplementation;
import service.FileManager;

import java.util.Locale;
import java.util.Scanner;

public class Application {

    public static final String PATH = "src/main/resources/demo.json";
    public static final String TYPE_FILTER = "Wheat";
    public static final String INGREDIENT_FILTER = "corn";
    Scanner scanner = new Scanner(System.in);
    private int outputFormat;

    public static void main(String[] args) {
        Application application = new Application();
        application.openConsol();

        BeerManager beerManager = new BeerManagerImplementation(PATH);


        String task1 = beerManager.groupBeersByBrand();
        String task2 = beerManager.filterBeersByBeerType(TYPE_FILTER);
        String task3 = beerManager.getTheCheapestBrand();
        String task4 = beerManager.getIdsThatLackSpecificIngredient(INGREDIENT_FILTER);
        String task5 = beerManager.sortAllBeersByRemainingIngredientRatio();
        String task6 = beerManager.listBeersBasedOnTheirPriceWithATip();

        System.out.println(task1);
        System.out.println(task2);
        System.out.println(task3);
        System.out.println(task4);
        System.out.println(task5);
        System.out.println(task6);
    }

    public void openConsol() {
        System.out.println("***********************\n   Beer Manager App\n***********************\nAdd your name:");
        String line = scanner.nextLine();
        while (validateName(line)) {
        }
        System.out.println("Select output format:\n1. Consol\n2. Write to Json file\n3. Write to database");
        int step;
        step = validateChoseLine(scanner.nextLine());
        switch (step){
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
        do {
            step = validateChoseLine(scanner.nextLine());
            switch (step){
                case 1:

            }

        }while (step != 7);
    }

    private int validateChoseLine(String nextLine) {
        for(Character c : nextLine.trim().toCharArray()){
            if(!Character.isDigit(c)){
                System.out.println("Given parameter is not number");
            }
        }
        return Integer.parseInt(nextLine);
    }

    private boolean validateName(String nextLine) {
        if (nextLine == null || nextLine.trim().equals("")) {
            System.out.println("Name not correct: " + nextLine);
            return true;
        } else {
            return false;
        }
    }
}
