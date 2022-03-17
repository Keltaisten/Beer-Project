package control;

import repository.BeerRepository;
import repository.WriteJson;
import repository.WriteToConsole;
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

//    private String task1;
//    private String task2;
//    private String task3;
//    private String task4;
//    private String task5;
//    private String task6;

    public static void main(String[] args) {
        Application application = new Application();

        BeerManager beerManager = new BeerManagerImplementation(PATH);
        application.openConsole(beerManager);

        String task1 = beerManager.groupBeersByBrand(application.outputFormat);
        String task2 = beerManager.filterBeersByBeerType(TYPE_FILTER, application.outputFormat);
        String task3 = beerManager.getTheCheapestBrand(application.outputFormat);
        String task4 = beerManager.getIdsThatLackSpecificIngredient(INGREDIENT_FILTER,application.outputFormat);
        String task5 = beerManager.sortAllBeersByRemainingIngredientRatio(application.outputFormat);
        String task6 = beerManager.listBeersBasedOnTheirPriceWithATip(application.outputFormat);

        System.out.println(task1);
        System.out.println(task2);
        System.out.println(task3);
        System.out.println(task4);
        System.out.println(task5);
        System.out.println(task6);
    }

    public void openConsole(BeerManager beerManager) {
        System.out.println("***********************\n   Beer Manager App\n***********************\nAdd your name:");
        String line = scanner.nextLine();
        while (validateName(line)) {
        }
        System.out.println("Select output format:\n1. Console\n2. Write to Json file\n3. Write to database");


        int step;
        step = validateChoseLine(scanner.nextLine());
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

        System.out.println("Select calculation:\n1. Console\n2. Write to Json file\n3. Write to database");

        do {
            step = validateChoseLine(scanner.nextLine());
            switch (step) {
                case 1:
                    String task1 = beerManager.groupBeersByBrand(outputFormat);
//                    selectOutPut(task1, outputFormat);
                    break;
                case 2:
                    String task2 = beerManager.filterBeersByBeerType(TYPE_FILTER, outputFormat);
                    break;
            }

        } while (step != 7);
    }

//    private void selectOutPut(String task, int outputFormat) {
//        if (outputFormat == 1) {
//            WriteToConsole writeToConsole = new WriteToConsole();
//            writeToConsole.writeToConsole(task);
//        } else if (outputFormat == 2) {
//            WriteJson writeJson = new WriteJson();
//            writeJson.writeToJsonFile(task);
//        } else if(outputFormat == 3){
//            BeerRepository beerRepository = new BeerRepository();
//            beerRepository.writeToDatabase(task);
//        }
//    }

    private int validateChoseLine(String nextLine) {
        for (Character c : nextLine.trim().toCharArray()) {
            if (!Character.isDigit(c)) {
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
