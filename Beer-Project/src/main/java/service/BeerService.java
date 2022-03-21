package service;

import beercatalog.BrandsWithBeers;

import java.util.List;
import java.util.Map;

public interface BeerService {
    List<BrandsWithBeers> groupBeersByBrand(int outputFormat);     //not finished
    List<String> filterBeersByBeerType(String type, int outputFormat);      //done
    String getTheCheapestBrand(int outputFormat);       //done
    List<String> getIdsThatLackSpecificIngredient(String ingredient, int outputFormat);     //done
    List<String> sortAllBeersByRemainingIngredientRatio(int outputFormat);        //maybe done
    Map<Integer, List<String>> listBeersBasedOnTheirPriceWithATip(int outputFormat);        //done
    void saveDataToDb();
}
