package service;

public interface BeerManager {
    String groupBeersByBrand(int outputFormat);     //not finished
    String filterBeersByBeerType(String type, int outputFormat);      //done
    String getTheCheapestBrand(int outputFormat);       //done
    String getIdsThatLackSpecificIngredient(String ingredient, int outputFormat);     //done
    String sortAllBeersByRemainingIngredientRatio(int outputFormat);        //maybe done
    String listBeersBasedOnTheirPriceWithATip(int outputFormat);        //done
}
