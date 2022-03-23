package repository;

import beercatalog.*;

import java.util.List;
import java.util.Optional;

public interface BeerRepo {
    void saveBeers(List<Beer> beers);
    void saveIngredients(Beer beer);
    Optional<List<String>> filterBeersByBeerTypeDb(String type);
    Optional<List<String>> getIdsThatLackSpecificIngredientDb(String ingredient);
    Optional<List<BeerAndPrice>> getBeersAndPricesForTheCheapestBrandDb();
    Optional<List<BeerIdWithIngredientRatio>> getBeerIdsWithIngrRatioForsortAllBeersByRemainingIngredientRatioDb();
    List<BrandsWithBeers> groupBeersByBrandDb();
    Optional<List<BeerAndPrice>> listBeersWithPriceForTipCalculationDb();
    List<BeerAndPrice> selectAndUpdatePrice();
    boolean deleteBeerByIdDb(String beerId);
    String findBeerNameByIdInBeers(String id);
    Double findRatioByIdInIngredients(long id);
    Double getRatioSumByIdInIngredients(String id);
    Beer findBeerByIdInBeers(String id);
    List<Ingredient> findIngredientsById(String id);
    boolean isABeerByIdInBeers(String id);
}
