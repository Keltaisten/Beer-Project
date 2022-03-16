package service;

public interface BeerManager {
    String groupBeersByBrand();     //not finished
    String filterBeersByBeerType(String type);      //done
    String getTheCheapestBrand();       //done
    String getIdsThatLackSpecificIngredient(String ingredient);     //done
    String sortAllBeersByRemainingIngredientRatio();        //maybe done
    String listBeersBasedOnTheirPriceWithATip();        //done


}
