package service;

import beercatalog.Beer;
import beercatalog.BrandsWithBeers;
import controller.enums.OutputFormat;
import repository.BeerRepo;

import java.util.List;
import java.util.Map;

public interface BeerService {
    List<BrandsWithBeers> groupBeersByBrand(OutputFormat outputFormat, String name);
    List<String> filterBeersByBeerType(String type, OutputFormat outputFormat, String name);
    String getTheCheapestBrand(OutputFormat outputFormat, String name);
    List<String> getIdsThatLackSpecificIngredient(String ingredient, OutputFormat outputFormat, String name);
    List<String> sortAllBeersByRemainingIngredientRatio(OutputFormat outputFormat, String name);
    Map<Integer, List<String>> listBeersBasedOnTheirPriceWithATip(OutputFormat outputFormat, String name);
    void saveDataToDb();
    void updatePrice();
    boolean deleteBeerById(String number);
    Integer roundPrice(int price);
    String convertToJson(Object o, int taskNumber, OutputFormat outputFormat, String nameOfTheTask, String name);
    void addBeer(Beer beer);
    void addBrandsWithBeers(BrandsWithBeers bwb);
    List<Beer> getBeers();
    FileService getFileManager();
    List<BrandsWithBeers> getBrandsWithBeers();
    BeerRepo getBeerRepo();
}
