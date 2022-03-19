package service;

public interface BeerManager {
    void groupBeersByBrand(int outputFormat);     //not finished
    void filterBeersByBeerType(String type, int outputFormat);      //done
    void getTheCheapestBrand(int outputFormat);       //done
    void getIdsThatLackSpecificIngredient(String ingredient, int outputFormat);     //done
    void sortAllBeersByRemainingIngredientRatio(int outputFormat);        //maybe done
    void listBeersBasedOnTheirPriceWithATip(int outputFormat);        //done
}
