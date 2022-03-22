package repository;

import beercatalog.Beer;
import beercatalog.BeerAndPrice;
import beercatalog.BeerIdWithIngredientRatio;
import beercatalog.BrandsWithBeers;

import java.util.List;
import java.util.Optional;

public interface BeerRepo {
    void saveBeers(List<Beer> beers);
    void saveIngredients(Beer beer);
    Optional<List<String>> filterBeersByBeerTypeDb(String type);
    Optional<List<String>> getIdsThatLackSpecificIngredientDb(String ingredient);
    Optional<List<BeerAndPrice>> getTheCheapestBrandDb();
    Optional<List<BeerIdWithIngredientRatio>> sortAllBeersByRemainingIngredientRatioDb();
    List<BrandsWithBeers> groupBeersByBrandDb();
    Optional<List<BeerAndPrice>> listBeersBasedOnTheirPriceWithATipDb();
}
