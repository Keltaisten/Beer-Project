package control;

import service.BeerManager;
import service.BeerManagerImplementation;
import service.FileManager;

public class Application {

    public static final String PATH = "src/main/resources/demo.json";
    public static final String TYPE_FILTER = "Wheat";
    public static final String INGREDIENT_FILTER = "corn";

    public static void main(String[] args) {
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
}
