package service;

import beercatalog.BrandsWithBeers;
import controller.OutputFormat;

import java.util.List;
import java.util.Map;

public interface BeerService {
    List<BrandsWithBeers> groupBeersByBrand(OutputFormat outputFormat);     //not finished
    List<String> filterBeersByBeerType(String type, OutputFormat outputFormat);      //done
    String getTheCheapestBrand(OutputFormat outputFormat);       //done
    List<String> getIdsThatLackSpecificIngredient(String ingredient, OutputFormat outputFormat);     //done
    List<String> sortAllBeersByRemainingIngredientRatio(OutputFormat outputFormat);        //maybe done
    Map<Integer, List<String>> listBeersBasedOnTheirPriceWithATip(OutputFormat outputFormat);        //done
    void saveDataToDb();
}
